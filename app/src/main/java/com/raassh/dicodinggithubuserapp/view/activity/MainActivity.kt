package com.raassh.dicodinggithubuserapp.view.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.raassh.dicodinggithubuserapp.*
import com.raassh.dicodinggithubuserapp.adapter.ListUserAdapter
import com.raassh.dicodinggithubuserapp.api.ListUsersResponse
import com.raassh.dicodinggithubuserapp.databinding.ActivityMainBinding
import com.raassh.dicodinggithubuserapp.misc.UserItem
import com.raassh.dicodinggithubuserapp.misc.visibility
import com.raassh.dicodinggithubuserapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    /*
    * Saya sudah membaca saran dari submission sebelumnya.
    * tetapi dari penjelasan artikel yang diberikan, cara mendeklarasikan binding dengan lateinit
    * hanya menyebabkan memory leaks jika dilakukan pada fragment.
    * bahkan dari dokumentasi ViewBinding, sample code untuk activity juga menggunakan lateinit
    * karena itu, saya tidak melakukan perubahan apa-apa untuk ViewBinding disini..
    * */
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

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

            errorMessage.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { text ->
                    Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService<SearchManager>()
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView

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

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                mainViewModel.searchUsers(MainViewModel.emptyQuery)

                return true
            }

        })

        return true
    }

    private fun setUsersData(listUsers: List<ListUsersResponse>) {
        val users = ArrayList<UserItem>()

        for (user in listUsers) {
            users.add(
                UserItem(
                    user.login,
                    user.avatarUrl
                )
            )
        }

        val listUserAdapter = ListUserAdapter(users)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserItem) {
                val detailsIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                    .apply {
                        putExtra(UserDetailActivity.EXTRA_USER, user)
                    }
                startActivity(detailsIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = visibility(isLoading)
            listUsers.visibility = visibility(!isLoading)
        }
    }
}