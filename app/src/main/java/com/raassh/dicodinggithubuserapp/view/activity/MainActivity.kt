package com.raassh.dicodinggithubuserapp.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raassh.dicodinggithubuserapp.R
import com.raassh.dicodinggithubuserapp.adapter.ListUserAdapter
import com.raassh.dicodinggithubuserapp.data.UserItem
import com.raassh.dicodinggithubuserapp.data.api.ListUsersResponse
import com.raassh.dicodinggithubuserapp.databinding.ActivityMainBinding
import com.raassh.dicodinggithubuserapp.misc.SettingPreferences
import com.raassh.dicodinggithubuserapp.misc.createUserArrayList
import com.raassh.dicodinggithubuserapp.misc.visibility
import com.raassh.dicodinggithubuserapp.viewmodel.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    /*
    * Saya sudah membaca saran dari submission sebelumnya.
    * tetapi dari penjelasan artikel yang diberikan, cara mendeklarasikan binding dengan lateinit
    * hanya menyebabkan memory leaks jika dilakukan pada fragment.
    * bahkan dari dokumentasi ViewBinding, sample code untuk activity juga menggunakan lateinit
    * karena itu, saya tidak melakukan perubahan apa-apa untuk ViewBinding disini..
    * */
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.apply {
            listUsers.observe(this@MainActivity) {
                setUsersData(it)
            }

            resultCount.observe(this@MainActivity) {
                binding.tvResultCount.text = getString(R.string.result_count, it)
            }

            isLoading.observe(this@MainActivity) {
                showLoading(it)
            }

            error.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { text ->
                    showError(text)
                }
            }

            canRetry.observe(this@MainActivity) {
                showRetry(it)
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.apply {
            rvUsers.apply {
                setHasFixedSize(true)
                this.layoutManager = layoutManager
                addItemDecoration(
                    DividerItemDecoration(
                        this@MainActivity,
                        layoutManager.orientation
                    )
                )
            }

            btnRetry.setOnClickListener { mainViewModel.searchUsers() }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                mainViewModel.searchUsers(MainViewModel.emptyQuery)

                return true
            }

        })

        val menuDarkMode = menu.findItem(R.id.dark_mode)

        mainViewModel.getThemeSettings().observe(this@MainActivity) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menuDarkMode.isChecked = true
                menuDarkMode.setIcon(R.drawable.ic_baseline_dark_mode_24)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menuDarkMode.isChecked = false
                menuDarkMode.setIcon(R.drawable.ic_outline_dark_mode_24)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.dark_mode -> {
                mainViewModel.saveThemeSetting(!item.isChecked)
                true
            }

            R.id.favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUsersData(listUsers: List<ListUsersResponse>) {
        val users = createUserArrayList(listUsers)

        binding.apply {
            rvUsers.adapter = ListUserAdapter(users).apply {
                setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: UserItem) {
                        val detailsIntent =
                            Intent(this@MainActivity, UserDetailActivity::class.java)
                                .apply {
                                    putExtra(UserDetailActivity.EXTRA_USER, user)
                                }
                        startActivity(detailsIntent)
                    }
                })
            }

            this.listUsers.visibility = visibility(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = visibility(isLoading)

            if (isLoading) {
                listUsers.visibility = visibility(false)
            }
        }
    }

    private fun showRetry(canRetry: Boolean) {
        binding.apply {
            btnRetry.visibility = visibility(canRetry)

            if (canRetry) {
                listUsers.visibility = visibility(false)
            }
        }
    }

    private fun showError(text: String) {
        Snackbar.make(binding.root, getString(R.string.error_message, text), Snackbar.LENGTH_SHORT)
            .setAction(R.string.try_again) { mainViewModel.searchUsers() }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    mainViewModel.setCanRetry()
                }
            })
            .show()
    }

}