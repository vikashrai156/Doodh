package com.example.doodhkahisab.ui
import android.app.Application
import androidx.lifecycle.*
import com.example.doodhkahisab.data.AppDatabase
import com.example.doodhkahisab.data.MilkRepository
import com.example.doodhkahisab.util.PricePreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val prefs = PricePreferences(application)
    private val repo = MilkRepository(db.milkDao(), prefs)

    fun addEntry(litres: Double, dateMillis: Long) { viewModelScope.launch { repo.insertEntry(getApplication(), dateMillis, litres) } }

    fun getEntriesForRange(start: Long, end: Long): LiveData<List<com.example.doodhkahisab.data.MilkEntry>> {
        val live = MutableLiveData<List<com.example.doodhkahisab.data.MilkEntry>>()
        viewModelScope.launch { repo.getEntriesBetween(start, end).collectLatest { live.postValue(it) } }
        return live
    }
}
