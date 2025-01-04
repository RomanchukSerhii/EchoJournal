package com.serhiiromanchuk.echojournal.presentation.screens.home.components

sealed interface EntryListPosition {
    data object First : EntryListPosition
    data object Middle : EntryListPosition
    data object Last : EntryListPosition
    data object Single : EntryListPosition
}