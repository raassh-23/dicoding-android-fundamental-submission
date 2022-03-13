package com.raassh.dicodinggithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raassh.dicodinggithubuserapp.databinding.ActivityMainBinding

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

//    private val list = ArrayList<User>()
//
//    private val listUsers: ArrayList<User>
//        get() {
//            val names = resources.getStringArray(R.array.name)
//            val usernames = resources.getStringArray(R.array.username)
//            val locations = resources.getStringArray(R.array.location)
//            val repos = resources.getStringArray(R.array.repository)
//            val companies = resources.getStringArray(R.array.company)
//            val followers = resources.getStringArray(R.array.followers)
//            val followings = resources.getStringArray(R.array.following)
//            val photos = resources.obtainTypedArray(R.array.avatar)
//
//            val tempList = ArrayList<User>()
//            for (i in names.indices) {
//                val user = User(
//                    usernames[i],
//                    names[i],
//                    locations[i],
//                    repos[i].toInt(),
//                    companies[i],
//                    followers[i].toInt(),
//                    followings[i].toInt(),
//                    photos.getResourceId(i, -1)
//                )
//
//                tempList.add(user)
//            }
//
//            photos.recycle()
//
//            return tempList
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.apply {
            setHasFixedSize(true)
            this.layoutManager = layoutManager
        }

        mainViewModel.listUsers.observe(this) {
            setUsersData(it)
        }

        mainViewModel.resultCount.observe(this) {
            binding.tvResultCount.text = getString(R.string.result_count, it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

//        list.addAll(listUsers)
//        showRecyclerList()
    }

    private fun setUsersData(listUsers: List<ListUsersResponseItem>) {
        val users = ArrayList<UserItem>()

        for (user in listUsers) {
            users.add(UserItem(
                user.login,
                user.avatarUrl
            ))
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
        fun visibility(visible: Boolean) = if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        binding.apply {
            progressBar.visibility = visibility(isLoading)
            tvResultCount.visibility = visibility(!isLoading)
            rvUsers.visibility = visibility(!isLoading)
        }
    }
}