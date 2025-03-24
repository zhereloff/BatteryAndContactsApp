package com.example.batteryandcontactsapp.contactsManager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.batteryandcontactsapp.MainActivity
import com.example.batteryandcontactsapp.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private val CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS
    private val REQUEST_CODE_CONTACTS = 101

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContactsAdapter
    private val contactsList = mutableListOf<Contact>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ContactsAdapter(contactsList)
        binding.recyclerView.adapter = adapter

        checkContactsPermission()
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.showMainLayout(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showMainLayout(true)
        _binding = null
    }

    private fun loadContacts() {
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
            null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(0)
                val phone = it.getString(1)
                contactsList.add(Contact(name, phone))
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), CONTACTS_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(CONTACTS_PERMISSION), REQUEST_CODE_CONTACTS)
        } else {
            loadContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CONTACTS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            Toast.makeText(requireContext(), "Permission to read contacts not granted", Toast.LENGTH_SHORT).show()
        }
    }
}
