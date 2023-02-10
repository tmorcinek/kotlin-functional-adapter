# Kotlin Functional Adapter Library for Android

<p>Language:   Kotlin</p>
<p>Platform:   Android</p>
<p>Developer:  Tomasz Morcinek</p>

[![](https://jitpack.io/v/tmorcinek/kotlin-functional-adapter.svg)](https://jitpack.io/#tmorcinek/kotlin-functional-adapter)


## Purpose
Using in combination with android extensions it simplifies working with RecyclerView. This library provides a way to write adapters in a functional way. 
There are several different types of adapters to help in different needs: 
 - List and Grid - Simple list of objects which have the same type. (supports LiveData)
 - Sections - List or grid with different types of objects. (supports LiveData)
 - Custom - Defining each item. This is a static list where you declare elements one by one.
 <br><br>
 
## Usage 
```groovy
    implementation 'com.github.tmorcinek:kotlin-functional-adapter:1.0'
```
<br><br>

## How it works

### List and Grid

#### List of strings supplied by **LiveData**
```kotlin
recyclerView.list<String>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }) {
    resId(R.layout.vh_name)
    onBind { _, item ->
        name.text = item
    }
    liveData(lifecycleOwner, namesLiveDate)
}
```
<br><br>
#### List of objects **City** 
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
    submitList(cities)
}
```
<br><br>
