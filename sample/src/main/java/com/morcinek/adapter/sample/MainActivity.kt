package com.morcinek.adapter.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.morcinek.android.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.mutable_collections.view.*
import kotlinx.android.synthetic.main.vh_city.view.*

class MainActivity : AppCompatActivity() {

    private val namesLiveDate = MutableLiveData(names)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.run {
            adapter = customAdapter {
                item(R.layout.welcome) {}
                itemView<RecyclerView>(R.layout.recycler_view) {
                    list<City>(itemCallback()) {
                        resId(R.layout.vh_city)
                        onBind { position, item ->
                            number.text = "${position + 1}."
                            name.text = item.name
                        }
                        submitList(cities)
                    }
                }
                itemView<RecyclerView>(R.layout.recycler_view) {
                    setup {
                        grid(2) { setupSpanSizeLookup { position -> if (position % 3 == 0) 2 else 1 } }
                        adapter(listAdapter<City>(itemCallback()) {
                            resId(R.layout.vh_city)
                            onBind { position, item ->
                                number.text = "${position + 1}."
                                name.text = item.name
                            }
                            submitList(listOf(
                                City("Barcelona"),
                                City("Warsaw"),
                                City("Krakow"),
                                City("Madrid"),
                                City("Lisbon"),
                            ))
                        })
                    }
                }
                itemView<RecyclerView>(R.layout.recycler_view) {
                    list<String>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }) {
                        resId(R.layout.vh_name)
                        onBind { _, item ->
                            name.text = item
                        }
                        submitList(names)
                    }
                }
                item {
                    resId(R.layout.mutable_collections)
                    onBind {
                        add.setOnClickListener { namesLiveDate.postValue(namesLiveDate.value?.run { plus("Element nr. ${size + 1}") }) }
                        recyclerView.list<String>(itemCallback { areItemsTheSame { s, s2 -> s == s2 } }) {
                            resId(R.layout.vh_name)
                            onBind { _, item ->
                                name.text = item
                            }
                            liveData(this@MainActivity, namesLiveDate)
                        }
                    }
                }
                itemView<RecyclerView>(R.layout.recycler_view) {
                    setup {
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
                            submitList(listOf(
                                Header("B"), City("Barcelona"), City("Beirut"),
                                Header("W"), City("Warsaw"),
                                Header("M"), City("Madrid"), City("Manchester"), City("Milan"), City("Moscow"),
                            ))
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
                else -> "Sections"
            }
        }.attach()
    }

    private fun citiesWithHeaders(): List<HasKey> = cities.groupBy { it.name.first() }.flatMap { listOf(Header("${it.key}")).plus(it.value) }
}

private val cities = listOf(
    City("Barcelona"),
    City("Warsaw"),
    City("Krakow"),
    City("Madrid"),
    City("Lisbon"),
    City("Porto"),
    City("Hamburg"),
    City("London"),
    City("Liverpool"),
    City("Manchester"),
    City("Paris"),
    City("Milan"),
    City("Turin"),
    City("Moscow"),
    City("Casablanca"),
    City("Beirut"),
)

private val names = listOf("Tomek", "Basia", "Kamil", "Krzysiu", "Karolina", "Beata", "Marek", "Grzegorz", "Mikolaj", "Wiktor")


private class City(val name: String) : HasKey {
    override val key: String get() = name
}

private class Header(val title: String, override val key: String = title) : HasKey