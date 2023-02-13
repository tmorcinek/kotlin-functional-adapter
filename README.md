# Kotlin Functional Adapter Library for Android

<p>Language:   Kotlin</p>
<p>Platform:   Android</p>
<p>Developer:  Tomasz Morcinek</p>

[![](https://jitpack.io/v/tmorcinek/kotlin-functional-adapter.svg)](https://jitpack.io/#tmorcinek/kotlin-functional-adapter)
<br>

## Purpose

Using in combination with **viewBinding**, it simplifies working with RecyclerView. This library provides a way to write adapters in a functional way. There are several different types of adapters to help in different needs:

- List and Grid - Simple list of objects which have the same type. (supports LiveData)
- Sections - List or grid with different types of objects. (supports LiveData)
- Custom - Defining each item. This is a static list where you declare elements one by one.
  <br><br>

## Gradle

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:
```groovy
implementation 'com.github.tmorcinek:kotlin-functional-adapter:1.0'
```

Enabling viewBinding in `app/build.gradle`:
```groovy
android {
    ...
    buildFeatures {
        viewBinding true
    }
    ...
}
```

<br>

## Usage <br>

### List and Grid <br>

- #### List of strings supplied by 'LiveData'

```kotlin
recyclerView.list<String, VhNameBinding>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }, VhNameBinding::inflate) {
    onBind { _, item ->
        name.text = item
    }
    liveData(this@MainActivity, namesLiveDate)
}

// LiveData declaration
private val namesLiveDate = MutableLiveData(listOf("Tomek", "Basia", "Kamil", "Krzysiu", "Karolina", "Beata"))
```
Simpified Generic Types
```kotlin
recyclerView.list(itemCallback<String> { areItemsTheSame { s, s2 -> s == s2 } }, VhNameBinding::inflate) {
    onBind { _, item ->
        name.text = item
    }
    liveData(this@MainActivity, namesLiveDate)
}
```

<br>

- #### List of objects 'City'

If we want to simplify code related to **itemCallback**

```kotlin
itemCallback { areItemsTheSame { s, s2 -> s == s2 } }
```

We have to implement **HasKey** interface in our **City** class

```kotlin
private class City(val name: String) : HasKey {
    override val key: String get() = name
}
```

RecyclerView code:

```kotlin
recyclerView.list(itemCallback<City>(), VhCityBinding::inflate) {
    onBind { position, item ->
        number.text = "${position + 1}."
        name.text = item.name
    }
    submitList(
        listOf(
            City("Barcelona"),
            City("Warsaw"),
            City("Krakow"),
            City("Madrid"),
            City("Lisbon"),
        )
    )
}
```

<br><br>

### Sections <br>

We have to implement **HasKey** interface in our **Header** class

```kotlin
private class Header(val title: String, override val key: String = title) : HasKey
```

RecyclerView code:

```kotlin
recyclerView.setup {
    adapter(sectionsAdapter {
        sectionBinding(VhNameBinding::inflate) { _, item: Header ->
            name.text = item.title
        }
        section(VhCityBinding::inflate) {
            onBind { _, item: City ->
                name.text = item.name
            }
        }
        grid(2) { setupSpanSizeLookup { position -> if (itemAtPositionIsClass<Header>(position)) 2 else 1 } }
        submitList(
            listOf(
                Header("B"), City("Barcelona"), City("Beirut"),
                Header("W"), City("Warsaw"),
                Header("M"), City("Madrid"), City("Manchester"), City("Milan"), City("Moscow"),
            )
        )
    })
}
```

Sections can be declared in a simple or extended ways:

```kotlin
// simple
sectionBinding(VhNameBinding::inflate) { _, item: Header ->
    name.text = item.title
}
// extended
section(VhNameBinding::inflate) {
    onBind { _, item: Header ->
        name.text = item.title
    }
}
```

<br><br>

### Custom <br>

Declaring custom elements for static list

```kotlin
recyclerView.setup {
    linear()
    adapter(customAdapter {
        itemBinding(VhNameBinding::inflate) { name.text = "B" }
        itemBinding(VhCityBinding::inflate) { name.text = "Barcelona" }
        itemBinding(VhCityBinding::inflate) { name.text = "Beirut" }
        itemBinding(VhNameBinding::inflate) { name.text = "W" }
        itemBinding(VhCityBinding::inflate) { name.text = "Warsaw" }
        itemBinding(VhNameBinding::inflate) { name.text = "K" }
        itemBinding(VhCityBinding::inflate) { name.text = "Krakow" }
    })
}
```

<br><br>

## Developer

<p>Github:    <a href="https://github.com/tmorcinek">tmorcinek</a></p>
<p>Linkedin:  <a href="https://www.linkedin.com/in/tmorcinek/">Tomasz Morcinek</a></p> 
<br><br>

## License

    Copyright 2023 Tomasz Morcinek.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
