package cubakoviclv1.ferit.ordercheaper.UI

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import cubakoviclv1.ferit.ordercheaper.R
import cubakoviclv1.ferit.ordercheaper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        auth = FirebaseAuth.getInstance()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val logInFragment = FragmentLogin()
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().add(R.id.frame_layout, FragmentLogin()).commit()


        navView.setNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.nav_login -> {
                    auth = FirebaseAuth.getInstance()
                    auth.currentUser
                    replaceFragment(FragmentLogin(), it.title.toString())
                }
                R.id.nav_account_settings -> replaceFragment(FragmentAccountSettings(), it.title.toString())
                R.id.nav_logout -> {
                    auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    replaceFragment(FragmentLogin(), it.title.toString())
                }
                R.id.nav_contact -> replaceFragment(FragmentContact(), it.title.toString())
                R.id.nav_order_now -> replaceFragment(FragmentOrderCheaper(), it.title.toString())
                R.id.nav_help -> replaceFragment(FragmentHelp(), it.title.toString())
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment, title: String) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            val currentUser = auth.currentUser
            if(currentUser == null) {
                navView.menu.findItem(R.id.nav_login).setVisible(true)
                navView.menu.findItem(R.id.nav_logout).setVisible(false)
                navView.menu.findItem(R.id.nav_account_settings).setVisible(false)
            } else {
                navView.menu.findItem(R.id.nav_login).setVisible(false)
                navView.menu.findItem(R.id.nav_logout).setVisible(true)
                navView.menu.findItem(R.id.nav_account_settings).setVisible(true)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}