package com.example.myfirebase.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.repositori.RepositorySiswa
import com.example.myfirebase.view.route.DestinasiDetail

sealed interface StatusUIDetail {
    data class Success(val satusiswa: Siswa?) : StatusUIDetail
    object Error : StatusUIDetail
    object Loading : StatusUIDetail
}

class DetailViewModel(savedStateHandle: SavedStateHandle, private val repositorySiswa: RepositorySiswa):
    ViewModel() {
        private val idSiswa: Long =
            savedStateHandle.get<String>(DestinasiDetail.itemIdArg)?.toLong()?: error("idSiswa tidak ditemukan di SavedStateHandle")
}