<div align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://github.com/donfreddy/troona/raw/develop/docs/images/troona-logotype-white.png">
    <img alt="Troona logo" src="https://github.com/donfreddy/troona/raw/develop/docs/images/troona-logotype-black.png">
  </picture>
</div>

<div align="center">

[![License](https://img.shields.io/badge/License-Apache--2.0-blue)](#license)
[![Go Report Card](https://goreportcard.com/badge/github.com/donfreddy/troona)](https://goreportcard.com/report/github.com/daytonaio/daytona)
[![Issues - troona](https://img.shields.io/github/issues/donfreddy/troona)](https://github.com/donfreddy/troona/issues)
![GitHub Release](https://img.shields.io/github/v/release/donfreddy/troona)
<br>
[![Open Bounties](https://img.shields.io/endpoint?url=https%3A%2F%2Fconsole.algora.io%2Fapi%2Fshields%2Fdaytonaio%2Fbounties%3Fstatus%3Dopen)](https://console.algora.io/org/daytonaio/bounties?status=open)
[![Rewarded Bounties](https://img.shields.io/endpoint?url=https%3A%2F%2Fconsole.algora.io%2Fapi%2Fshields%2Fdaytonaio%2Fbounties%3Fstatus%3Dcompleted)](https://console.algora.io/org/daytonaio/bounties?status=completed)

</div>

<div align="center">
**Troona** is a music player that lets you play music from your local device. It is built
entirely with kotlin and Jetpack Composer.
</div>

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=com.troona">
    <img alt="Get it on Google Play" title="Google Play" src="http://i.imgur.com/mtGRPuM.png" width="140">
  </a>
</p>

<br>
:star: Star us on GitHub — it motivates us a lot!

---

## Buy me a coffee

Whether you use this project, have learned something from it, or just like it, please consider
supporting it by buying me a coffee, so I can dedicate more time on open-source projects like
this :)

<a href="https://www.buymeacoffee.com/donfreddy" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>

---

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Running the project locally](#running-the-project-locally)
    - [Prerequisites](#prerequisites)
    - [Setup](#setup)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- [] Play music from your local device
- [x]  Shuffle and repeat
- [] Playlists
- [x] Dark mode support
- [] Search for music
- [] Play music in the background
- [] Control music from the lock screen

## Screenshots

## Running the project locally

### Prerequisites

- Android Studio (latest version recommended)
- Kotlin 1.5 or higher
- Gradle 7.0.2 or higher

### Setup

1. Clone the repository to your local machine
2. Open the project in Android Studio
3. Build and run the project
4. Enjoy!

## Architecture

The **Troona** app follows
the [official architecture guidance](https://developer.android.com/topic/architecture) and is built
using:

- [Jetpack Compose](https://developer.android.com/jetpack/compose) UI toolkit
- [Material 2](https://m2.material.io/) design guidelines
- [Room](https://developer.android.com/jetpack/androidx/releases/room) for data persistence
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) for managing background
  threads
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency
  injection
- And more!

__The **troona** app has been fully modularized.__

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process
for submitting pull requests to us.

## License

```markdown
Copyright 2024 Don Freddy

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

## Acknowledgments