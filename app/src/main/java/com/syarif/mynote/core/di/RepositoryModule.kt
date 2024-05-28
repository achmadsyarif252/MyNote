package com.syarif.mynote.core.di

import com.syarif.mynote.core.data.repository.MyNoteRepository
import com.syarif.mynote.core.domain.repository.IMyNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(myNoteRepository: MyNoteRepository): IMyNoteRepository
}