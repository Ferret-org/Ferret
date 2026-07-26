package com.ferret

import com.ferret.repository.NetworkRecordRepository


internal expect class FerretRepository {
    val networkRecordRepository: NetworkRecordRepository
}