package cz.mmaso.kiosekdb.scanner

import cz.mmaso.kiosekdb.Passport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


object PassportScannerBroker {
    private val _passport = MutableStateFlow<List<Passport>>(emptyList())
    val passport = _passport.asStateFlow()

    fun addRequest( passport: Passport ) {
        _passport.update { it + passport }
    }

    fun clear() {
        _passport.value = emptyList()
    }

    fun getLast() : Passport? {
        if( _passport.value.size > 0 ) {
            return _passport.value.last()
            clear()
        }
        return null;
    }
}