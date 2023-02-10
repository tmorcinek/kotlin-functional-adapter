# Kotlin Functional Adapter Library for Android

<p>Language:   Kotlin</p>
<p>Platform:   Android</p>
<p>Developer:  Tomasz Morcinek</p>

[![](https://jitpack.io/v/tmorcinek/kotlin-functional-adapter.svg)](https://jitpack.io/#tmorcinek/kotlin-functional-adapter)
<br>

## Purpose

Using in combination with android extensions it simplifies working with RecyclerView. This library provides a way to write adapters in a functional way. There
are several different types of adapters to help in different needs:

- List and Grid - Simple list of objects which have the same type. (supports LiveData)
- Sections - List or grid with different types of objects. (supports LiveData)
- Custom - Defining each item. This is a static list where you declare elements one by one.
  <br><br>

## Gradle

```groovy
    implementation 'com.github.tmorcinek:kotlin-functional-adapter:1.0'
```

<br>

## Usage <br>

### List and Grid <br>

- #### List of strings supplied by 'LiveData'

```kotlin
recyclerView.list<String>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }) {
    resId(R.layout.vh_name)
    onBind { _, item ->
        name.text = item
    }
    liveData(lifecycleOwner, namesLiveDate)
}

// LiveData declaration
private val namesLiveDate = MutableLiveData(listOf("Tomek", "Basia", "Kamil", "Krzysiu", "Karolina", "Beata"))
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
recyclerView.list<City>(itemCallback()) {
    resId(R.layout.vh_city)
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
        section<Header>(R.layout.vh_name) { _, item ->
            name.text = item.title
        }
        section<City> {
            resId(R.layout.vh_city)
            onBindView { _, item ->
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
section<Header>(R.layout.vh_name) { _, item ->
    name.text = item.title
}
// extended
section<Header> {
    resId(R.layout.vh_name)
    onBindView { _, item ->
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
        item(R.layout.vh_name) { name.text = "B" }
        item(R.layout.vh_city) { name.text = "Barcelona" }
        item(R.layout.vh_city) { name.text = "Beirut" }
        item(R.layout.vh_name) { name.text = "W" }
        item(R.layout.vh_city) { name.text = "Warsaw" }
        item(R.layout.vh_name) { name.text = "K" }
        item(R.layout.vh_city) { name.text = "Krakow" }
    })
}
```

<br><br>

## Developed By

<p>Tomasz Morcinek</p>
<p>Github:    <a href="https://github.com/tmorcinek">tmorcinek</a></p>
<p>Linkedin:  <a href="https://www.linkedin.com/in/tmorcinek/">tmorcinek</a></p> 
<br><br>

## License

    Copyright 2023 Tomasz Morcinek.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http:/``/www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
