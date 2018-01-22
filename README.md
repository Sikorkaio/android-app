Sikorka Android Application
---------

### Geth Android Library

The go-ethereum android aar is build from [https://github.com/kelsos/go-ethereum/tree/sikorka-abi-unmarshal-fix](https://github.com/kelsos/go-ethereum/tree/sikorka-abi-unmarshal-fix)
due to issue 14832 of go-ethereum that causes `'abi: cannot unmarshal x in to []interface {}'` when
trying to interact with smart contracts.

The library is deployed on bintray with a different `groupId` and `artifactId` from the artifact deployed
on maven central in order to avoid confusion.

As soon as the issue is fixed upstream the dependency will be updated to the official artifact. 

### Google Maps 
For google maps integration you should provide a `api-keys.properties` file in the project `root directory`

The file should have the following format
```properties
google_maps_key=API_KEY_VALUE
```


### Building

In order to build this project, the latest canary build of Android Studio (3.x) is required.

For more information please check the [Build](GUIDE/build.md) instructions.

### Ropsten syncing

If you have problems with ropsten please check [here](GUIDE/peers.md)

License
---------

    BSD 3-Clause License
    
    Copyright (c) 2017, Sikorka Team
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:
    
    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.
    
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.
    
    * Neither the name of the copyright holder nor the names of its
      contributors may be used to endorse or promote products derived from
      this software without specific prior written permission.
    
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.