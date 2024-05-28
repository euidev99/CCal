pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }

}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "CCal"
//include(":app")
//include(":xrmanifest.androidlib")
//project(":unityLibrary").projectDir = file("./unityLibrary")
//project(":xrmanifest.androidlib").projectDir = file("./unityLibrary/xrmanifest.androidlib")
//include(":app", ":unityLibrary") //, "unityLibrary:xrmanifest.androidlib")
include(":app", ":unityLibrary", "unityLibrary:xrmanifest.androidlib")


