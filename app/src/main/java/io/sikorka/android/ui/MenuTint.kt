@file:Suppress("unused")
package io.sikorka.android.ui

/*
 * This is the license of the original java class where this class was derived from.
 *
 * Copyright (C) 2015. Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PorterDuff
import android.support.v7.view.menu.MenuItemImpl
import android.support.v7.widget.ActionMenuView
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import timber.log.Timber
import java.lang.reflect.Method

/**
 * Apply colors and/or transparency to menu icons in a [Menu].
 *
 * Example usage:
 * <pre class="prettyprint">
 * public boolean onCreateOptionsMenu(Menu menu) {
 * ...
 * int color = getResources().getColor(R.color.your_awesome_color);
 * int alpha = 204; // 80% transparency
 * MenuTint.on(menu).setMenuItemIconColor(color).setMenuItemIconAlpha(alpha).apply(this);
 * ...
 * }
</pre> *
 */
class MenuTint private constructor(builder: MenuTint.Builder) {
  val menu: Menu
  private val originalMenuItemIconColor: Int?
  private val menuItemIconAlpha: Int?
  private val subMenuIconColor: Int?
  private val subMenuIconAlpha: Int?
  private val overflowDrawableId: Int?
  private val reApplyOnChange: Boolean
  private val forceIcons: Boolean
  private var menuItemIconColor: Int? = null
  var overflowMenuButton: ImageView? = null
    private set
  private var actionBarView: ViewGroup? = null

  init {
    menu = builder.menu
    originalMenuItemIconColor = builder.originalMenuItemIconColor
    menuItemIconColor = builder.menuItemIconColor
    menuItemIconAlpha = builder.menuItemIconAlpha
    subMenuIconColor = builder.subMenuIconColor
    subMenuIconAlpha = builder.subMenuIconAlpha
    overflowDrawableId = builder.overflowDrawableId
    reApplyOnChange = builder.reApplyOnChange
    forceIcons = builder.forceIcons
  }


  /**
   *
   * Sets a ColorFilter and/or alpha on all the [MenuItem]s in the menu, including the OverflowMenuButton.
   *
   * Call this method after inflating/creating your menu in [Activity.onCreateOptionsMenu].
   *
   * Note: This is targeted for the native ActionBar/Toolbar, not AppCompat.
   * @param activity the activity to apply the menu tinting on.
   */
  @SuppressLint("RestrictedApi")
  fun apply(activity: Activity) {

    if (forceIcons) {
      forceMenuIcons(menu)
    }

    run {
      var i = 0
      val size = menu.size()
      while (i < size) {
        val item = menu.getItem(i)
        colorMenuItem(item, menuItemIconColor, menuItemIconAlpha)
        if (reApplyOnChange) {
          val view = item.actionView
          if (view != null) {
            item.setOnActionExpandListener(NativeActionExpandListener(this))
          }
        }
        i++
      }
    }

    actionBarView = findActionBar(activity)

    // We must wait for the view to be created to set a color filter on the drawables.
    actionBarView?.post {
      var i = 0
      val size = menu.size()
      while (i < size) {
        val menuItem = menu.getItem(i)
        if (isInOverflow(menuItem)) {
          colorMenuItem(menuItem, subMenuIconColor, subMenuIconAlpha)
        }
        if (menuItem.hasSubMenu()) {
          val subMenu = menuItem.subMenu
          for (j in 0 until subMenu.size()) {
            colorMenuItem(subMenu.getItem(j), subMenuIconColor, subMenuIconAlpha)
          }
        }
        i++
      }

      if (menuItemIconColor != null || menuItemIconAlpha != null) {
        overflowMenuButton = findOverflowMenuButton(activity, actionBarView)
        colorOverflowMenuItem(overflowMenuButton)
      }
    }
  }

  /**
   *
   * Sets a ColorFilter and/or alpha on all the [MenuItem]s in the menu, including the
   * OverflowMenuButton.

   *
   * This should only be called after calling [.apply]. It is useful for when
   * [MenuItem]s might be re-arranged due to an action view being collapsed or expanded.
   */
  fun reapply() {
    run {
      var i = 0
      val size = menu.size()
      while (i < size) {
        val item = menu.getItem(i)
        if (isActionButton(item)) {
          colorMenuItem(menu.getItem(i), menuItemIconColor, menuItemIconAlpha)
        }
        i++
      }
    }

    actionBarView?.post {
      var i = 0
      val size = menu.size()
      while (i < size) {
        val menuItem = menu.getItem(i)
        if (isInOverflow(menuItem)) {
          colorMenuItem(menuItem, subMenuIconColor, subMenuIconAlpha)
        } else {
          colorMenuItem(menu.getItem(i), menuItemIconColor, menuItemIconAlpha)
        }
        if (menuItem.hasSubMenu()) {
          val subMenu = menuItem.subMenu
          for (j in 0 until subMenu.size()) {
            colorMenuItem(subMenu.getItem(j), subMenuIconColor, subMenuIconAlpha)
          }
        }
        i++
      }
      if (menuItemIconColor != null || menuItemIconAlpha != null) {
        colorOverflowMenuItem(overflowMenuButton)
      }
    }
  }

  private fun colorOverflowMenuItem(overflow: ImageView?) {
    if (overflow != null) {
      if (overflowDrawableId != null) {
        overflow.setImageResource(overflowDrawableId)
      }
      if (menuItemIconColor != null) {
        overflow.setColorFilter(menuItemIconColor!!)
      }
      menuItemIconAlpha?.let {
        overflow.imageAlpha = it
      }
    }
  }

  fun setMenuItemIconColor(color: Int?) {
    menuItemIconColor = color
  }

  class NativeActionExpandListener(private val menuTint: MenuTint) : OnActionExpandListener {

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
      val color = menuTint.originalMenuItemIconColor ?: menuTint.menuItemIconColor
      menuTint.setMenuItemIconColor(color)
      menuTint.reapply()
      return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
      val color = menuTint.originalMenuItemIconColor ?: menuTint.menuItemIconColor
      menuTint.setMenuItemIconColor(color)
      menuTint.reapply()
      return true
    }
  }

  // --------------------------------------------------------------------------------------------

  class Builder internal constructor(internal val menu: Menu) {
    internal var menuItemIconColor: Int? = null
    internal var menuItemIconAlpha: Int? = null
    internal var subMenuIconColor: Int? = null
    internal var subMenuIconAlpha: Int? = null
    internal var overflowDrawableId: Int? = null
    internal var originalMenuItemIconColor: Int? = null
    internal var reApplyOnChange: Boolean = false
    internal var forceIcons: Boolean = false

    /**
     *
     * Sets an [OnActionExpandListener] on all [MenuItem]s with views, so when the
     * menu is updated, the colors will be also.
     *
     * This is useful when the overflow menu is showing icons and [MenuItem]s might be
     * pushed to the overflow menu when a action view is expanded e.g. android.widget.SearchView.
     *
     * @param reapply `true` to set the listeners on all [MenuItem]s with action views.
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun reapplyOnChange(reapply: Boolean): Builder = apply { reApplyOnChange = reapply }

    /**
     * Specify an alpha value for visible MenuItem icons, including the OverflowMenuButton.
     *
     * @param alpha the alpha value for the drawable. 0 means fully transparent, and 255 means fully opaque.
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun setMenuItemIconAlpha(alpha: Int): Builder = apply { menuItemIconAlpha = alpha }

    /**
     * Specify a color for visible MenuItem icons, including the OverflowMenuButton.
     *
     * @param color the color to apply on visible MenuItem icons, including the OverflowMenuButton.
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun setMenuItemIconColor(color: Int): Builder = apply { menuItemIconColor = color }

    /**
     * Specify a color that is applied when an action view is expanded or collapsed.

     * @param color the color to apply on MenuItems when an action-view is expanded or collapsed.
     * *
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun setOriginalMenuItemIconColor(color: Int): Builder = apply { originalMenuItemIconColor = color }

    /**
     * Set the drawable id to set on the OverflowMenuButton.
     *
     * @param drawableId the resource identifier of the drawable
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun setOverflowDrawableId(drawableId: Int): Builder = apply { overflowDrawableId = drawableId }

    /**
     * Specify an alpha value for MenuItems that are in a SubMenu or in the Overflow menu.
     *
     * @param alpha the alpha value for the drawable. 0 means fully transparent, and 255 means fully opaque.
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun setSubMenuIconAlpha(alpha: Int): Builder = apply { subMenuIconAlpha = alpha }

    /**
     * Specify a color for MenuItems that are in a SubMenu or in the Overflow menu.
     *
     * @param color the color to apply on visible MenuItem icons, including the OverflowMenuButton.
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun setSubMenuIconColor(color: Int): Builder = apply { subMenuIconColor = color }

    /**
     * Set the menu to show MenuItem icons in the overflow window.
     *
     * @return this Builder object to allow for chaining of calls to set methods
     */
    fun forceIcons(): Builder = apply { forceIcons = true }

    /**
     *
     * Sets a ColorFilter and/or alpha on all the MenuItems in the menu, including the OverflowMenuButton.
     *
     * Call this method after inflating/creating your menu in [Activity.onCreateOptionsMenu].
     *
     * Note: This is targeted for the native ActionBar/Toolbar, not AppCompat.
     */
    fun apply(activity: Activity): MenuTint {
      val theme = MenuTint(this)
      theme.apply(activity)
      return theme
    }

    /**
     *
     * Creates a [MenuTint] with the arguments supplied to this builder.
     *
     * It does not apply the theme. Call [MenuTint.apply] to do so.
     * @see .apply
     */
    fun create(): MenuTint = MenuTint(this)
  }

  // --------------------------------------------------------------------------------------------

  /**
   * Auto collapses the SearchView when the soft keyboard is dismissed.
   */
  class SearchViewFocusListener(private val item: MenuItem?) : View.OnFocusChangeListener {

    override fun onFocusChange(v: View, hasFocus: Boolean) {
      if (!hasFocus && item != null) {
        item.collapseActionView()
        (v as? SearchView)?.setQuery("", false)
      }
    }
  }

  companion object {
    private val TAG = "MenuTint"
    private var nativeIsActionButton: Method? = null

    /**
     * Check if an item is showing (not in the overflow menu).
     * @param item the MenuItem.
     * @return `true` if the MenuItem is visible on the ActionBar.
     */
    @SuppressLint("RestrictedApi", "PrivateApi")
    fun isActionButton(item: MenuItem): Boolean {
      if (item is MenuItemImpl) {
        return item.isActionButton
      }

      if (nativeIsActionButton == null) {
        try {
          val MenuItemImpl = Class.forName("com.android.internal.view.menu.MenuItemImpl")
          nativeIsActionButton = MenuItemImpl.getDeclaredMethod("isActionButton")

          if (!nativeIsActionButton!!.isAccessible) {
            nativeIsActionButton!!.isAccessible = true
          }

        } catch (ignored: Exception) {
        }

      }
      try {
        return nativeIsActionButton!!.invoke(item, null) as Boolean
      } catch (e: Exception) {
        Timber.v("action button: %s", e.message)
      }

      return true
    }

    /**
     * Check if an item is in the overflow menu.
     *
     * @param item the MenuItem
     * @return `true` if the MenuItem is in the overflow menu.
     * @see .isActionButton
     */
    fun isInOverflow(item: MenuItem): Boolean {
      return !isActionButton(item)
    }

    /**
     * Sets the color filter and/or the alpha transparency on a [MenuItem]'s icon.
     *
     * @param menuItem The [MenuItem] to theme.
     * @param color The color to set for the color filter or `null` for no changes.
     * @param alpha The alpha value (0...255) to set on the icon or `null` for no changes.
     */
    fun colorMenuItem(menuItem: MenuItem, color: Int?, alpha: Int?) {
      if (color == null && alpha == null) {
        return  // nothing to do.
      }
      val drawable = menuItem.icon
      if (drawable != null) {
        // If we don't mutate the drawable, then all drawables with this id will have the ColorFilter
        drawable.mutate()
        if (color != null) {
          drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
        if (alpha != null) {
          drawable.alpha = alpha
        }
      }
    }

    /**
     * Set the menu to show MenuItem icons in the overflow window.

     * @param menu the menu to force icons to show
     */
    fun forceMenuIcons(menu: Menu) {
      try {
        val MenuBuilder = menu.javaClass
        val setOptionalIconsVisible = MenuBuilder.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
        if (!setOptionalIconsVisible.isAccessible) {
          setOptionalIconsVisible.isAccessible = true
        }
        setOptionalIconsVisible.invoke(menu, true)
      } catch (ignored: Exception) {
      }

    }

    fun on(menu: Menu): Builder {
      return Builder(menu)
    }

    /**
     * Apply a ColorFilter with the specified color to all icons in the menu.
     * @param activity the Activity.
     * @param menu the menu after items have been added.
     * @param color the color for the ColorFilter.
     */
    fun colorIcons(activity: Activity, menu: Menu, color: Int) {
      MenuTint.on(menu).setMenuItemIconColor(color).apply(activity)
    }

    /**
     *
     * @param activity the Activity
     * @return the OverflowMenuButton or `null` if it doesn't exist.
     */
    fun getOverflowMenuButton(activity: Activity): ImageView? {
      return findOverflowMenuButton(activity, findActionBar(activity))
    }

    private fun findOverflowMenuButton(activity: Activity, viewGroup: ViewGroup?): ImageView? {
      if (viewGroup == null) {
        return null
      }
      var overflow: ImageView? = null
      var i = 0
      val count = viewGroup.childCount
      while (i < count) {
        val v = viewGroup.getChildAt(i)
        if (v is ImageView && (v.javaClass.simpleName == "OverflowMenuButton" || v is ActionMenuView.ActionMenuChildView)) {
          overflow = v
        } else if (v is ViewGroup) {
          overflow = findOverflowMenuButton(activity, v)
        }
        if (overflow != null) {
          break
        }
        i++
      }
      return overflow
    }

    private fun findActionBar(activity: Activity): ViewGroup? {
      val id = activity.resources.getIdentifier("action_bar", "id", "android")
      var actionBar: ViewGroup? = null
      if (id != 0) {
        actionBar = activity.findViewById<ViewGroup?>(id)
      }
      if (actionBar == null) {
        actionBar = findToolbar(activity.findViewById<ViewGroup>(android.R.id.content).rootView as ViewGroup)
      }
      return actionBar
    }

    private fun findToolbar(viewGroup: ViewGroup): ViewGroup? {
      var toolbar: ViewGroup? = null
      var i = 0
      val len = viewGroup.childCount
      while (i < len) {
        val view = viewGroup.getChildAt(i)
        val isAppCompatToolbar = view.javaClass == android.support.v7.widget.Toolbar::class.java
        val isToolbar = view.javaClass.name == "android.widget.Toolbar"
        if (isAppCompatToolbar || isToolbar) {
          toolbar = view as ViewGroup
        } else if (view is ViewGroup) {
          toolbar = findToolbar(view)
        }
        if (toolbar != null) {
          break
        }
        i++
      }
      return toolbar
    }
  }
}
