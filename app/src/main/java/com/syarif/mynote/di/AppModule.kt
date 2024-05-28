package com.syarif.mynote.di

import com.syarif.mynote.core.domain.usecase.MyNoteInteractor
import com.syarif.mynote.core.domain.usecase.MyNoteUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    @ViewModelScoped
    abstract fun provideMyNoteUseCase(myNoteInteractor: MyNoteInteractor): MyNoteUseCase
}