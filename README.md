# HyperLap2D Common API

Common API for building HyperLap2D's plugins

### Integration

#### Gradle
![maven-central](https://img.shields.io/maven-central/v/games.rednblack.hyperlap2d/common-api?color=blue&label=release)
![sonatype-nexus](https://img.shields.io/nexus/s/games.rednblack.hyperlap2d/common-api?label=snapshot&server=https%3A%2F%2Foss.sonatype.org)

Runtime needs to be included into your project.
```groovy
dependencies {
    api "games.rednblack.hyperlap2d:common-api:$h2dVersion"
}
```

#### Maven
```xml
<dependency>
  <groupId>games.rednblack.hyperlap2d</groupId>
  <artifactId>common-api</artifactId>
  <version>0.1.3</version>
  <type>pom</type>
</dependency>
```

### License
HyperLap2D's Common API is licensed under the Apache 2.0 License. You can use it free of charge, without limitations both in commercial and non-commercial projects. We love to get (non-mandatory) credit in case you release a game or app using HyperLap2D!

```
Copyright (c) 2020 Francesco Marongiu.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

#### Overlap2D

HyperLap2D common API is a fork of [overlap2d-common-api](https://github.com/UnderwaterApps/overlap2d/tree/master/overlap2d-common-api). A very special thanks to UnderwaterApps's Team and all of their Contributors for creating it, as without, HyperLap2D could never be possible.
Check out original: [`OVERLAP2D-AUTHORS`](https://github.com/rednblackgames/HyperLap2D/blob/master/OVERLAP2D-AUTHORS) and [`OVERLAP2D-CONTRIBUTORS`](https://github.com/rednblackgames/HyperLap2D/blob/master/OVERLAP2D-CONTRIBUTORS)

_overlap2d-common-api_ was licensed under `Apache 2.0`
```
Copyright 2015 Underwater Apps LLC
( https://github.com/UnderwaterApps/overlap2d-runtime-libgdx  http://overlap2d.com )

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```