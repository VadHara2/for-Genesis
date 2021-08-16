package com.books.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    //Не знаю навіщо тут DI ¯\_(ツ)_/¯
    //Я це використовую для Retrofit2 і Room Database
    //Думав, що тре буде кешувати дані з remote config, але воно робить це само
    //Додав у проекц Dagger Hilt лише щоб показати, що вмію)
}