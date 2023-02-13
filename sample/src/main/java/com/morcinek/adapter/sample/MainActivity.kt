package com.morcinek.adapter.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.morcinek.adapter.sample.databinding.*
import com.morcinek.android.*

class MainActivity : AppCompatActivity() {

    private val namesLiveDate = MutableLiveData(listOf("Tomek", "Basia", "Kamil", "Krzysiu", "Karolina", "Beata"))

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            viewPager.run {
                adapter = customAdapter {
                    item(WelcomeBinding::inflate)
                    itemBinding(RecyclerViewBinding::inflate) {
                        root.list(itemCallback<City>(), VhCityBinding::inflate) {
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
                                    City("Porto"),
                                    City("Hamburg"),
                                    City("London"),
                                )
                            )
                        }
                    }
                    itemBinding(RecyclerViewBinding::inflate) {
                        root.setup {
                            grid(2) { setupSpanSizeLookup { position -> if (position % 3 == 0) 2 else 1 } }
                            adapter(listAdapter<City, VhCityBinding>(itemCallback(), VhCityBinding::inflate) {
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
                                        City("Porto"),
                                        City("Hamburg"),
                                        City("London"),
                                    )
                                )
                            })
                        }
                    }
                    itemBinding(RecyclerViewBinding::inflate) {
                        root.list<String, VhNameBinding>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }, VhNameBinding::inflate) {
                            onBind { _, item ->
                                name.text = item
                            }
                            submitList(listOf("Tomek", "Basia", "Kamil", "Krzysiu", "Karolina", "Beata", "Marek", "Grzegorz", "Mikolaj", "Wiktor"))
                        }
                    }
                    item(MutableCollectionsBinding::inflate) {
                        onBind {
                            add.setOnClickListener { namesLiveDate.postValue(namesLiveDate.value?.run { plus("Element nr. ${size + 1}") }) }
                            recyclerView.list(itemCallback<String> { areItemsTheSame { s, s2 -> s == s2 } }, VhNameBinding::inflate) {
                                onBind { _, item ->
                                    name.text = item
                                }
                                liveData(this@MainActivity, namesLiveDate)
                            }
                        }
                    }
                    itemBinding(RecyclerViewBinding::inflate) {
                        root.setup {
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
                    }
                    itemBinding(RecyclerViewBinding::inflate) {
                        root.setup {
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
                    }
                }
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Welcome"
                    1 -> "List"
                    2 -> "Grid"
                    3 -> "Strings"
                    4 -> "LiveData"
                    5 -> "Sections"
                    else -> "Custom"
                }
            }.attach()
        }
    }
}

private class City(val name: String) : HasKey {
    override val key: String get() = name
}

private class Header(val title: String, override val key: String = title) : HasKey