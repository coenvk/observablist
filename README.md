# Observablist :eyeglasses:

> An Android library for observable lists ultimately used with RecyclerView Adapters.

![Bintray](https://img.shields.io/bintray/v/coenvk/maven/observablist?label=Download)
![GitHub](https://img.shields.io/github/license/coenvk/observablist?label=License)
![GitHub stars](https://img.shields.io/github/stars/coenvk/observablist?style=social)

# Table of Contents  
  
- [Getting Started](#getting-started)  
- [Usage](#usage)  
	- [Basic](#basic)
  - [RecyclerView Adapter](#recyclerview-adapter)
- [Build](#build)
- [License](#license)

## Getting Started  
  
1. Add the `jcenter()` repository to the project `build.gradle` file.  
  
```  
buildscript {  
 repositories { jcenter() }}  
```  
  
2. Add the dependency to your module's `build.gradle` file.  
  
>***Latest version:*** ![Bintray](https://img.shields.io/bintray/v/coenvk/maven/observablist?label=Download)
  
```  
dependencies {  
 implementation 'com.coenvk.android.observablist:observablist:1.0.0'}  
```

## Usage

### Basic

The `ObservableList` can be used to create a list which can be observed by a `DataObserver`. The list extends the `MutableList` class, so it can be used with the same functionality. For better performance, the item class can implement `Diffable` and implement the `areItemsTheSame` and `areContentsTheSame` methods to compare two items.

<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
val observableList = ObservableList<Any>("a", "b", 1, 2, 3)
val dataObserver = object : DataObserver() {
  override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
    println("Items were removed.")
  }
}
observableList.registerObserver(dataObserver)
observableList.remove("a")

// Output:
// Items were removed.
```` 
</p></details>

### RecyclerView Adapter

The `ObservableList` is perfect for usage with a (RecyclerView) Adapter. Simply register an observer that notifies changes to the adapter and the view is automatically updated.

<details><summary><b>Kotlin (click to expand)</b></summary>  
<p>  
  
````kotlin  
val dataObserver = object : DataObserver() {
  override fun onChanged() {
    adapter.notifyDataSetChanged()
  }
  override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
    adapter.notifyItemRangeRemoved(positionStart, itemCount)
  }
  override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
    adapter.notifyItemRangeInserted(positionStart, itemCount)
  }
  override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
    adapter.run {
      for (i in 0 until itemCount) notifyItemMoved(fromPosition + i, toPosition + i)
    }
  }
  override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
    adapter.notifyItemRangeChanged(positionStart, itemCount)
  }
}
observableList.registerObserver(dataObserver)
```` 
</p></details>

## Build
The project can be build locally using the following commands:
```bash
$ git clone https://github.com/coenvk/observablist.git
$ cd observablist
$ ./gradlew clean build
```
  
## License  
  
Observablist is licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0).
