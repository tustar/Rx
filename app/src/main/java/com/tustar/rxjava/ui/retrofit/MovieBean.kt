package com.tustar.rxjava.ui.retrofit

data class MovieBean(
        val count: Int,
        val start: Int,
        val subjects: List<Subject>,
        val title: String,
        val total: Int
)

data class Subject(
        val alt: String,
        val casts: List<Cast>,
        val collect_count: Int,
        val directors: List<Director>,
        val genres: List<String>,
        val id: String,
        val images: Images,
        val original_title: String,
        val rating: Rating,
        val subtype: String,
        val title: String,
        val year: String
)

data class Rating(
        val average: Double,
        val max: Int,
        val min: Int,
        val stars: String
)

data class Cast(
        val alt: String,
        val avatars: Avatars,
        val id: String,
        val name: String
)

data class Avatars(
        val large: String,
        val medium: String,
        val small: String
)

data class Images(
        val large: String,
        val medium: String,
        val small: String
)

data class Director(
        val alt: String,
        val avatars: Avatars,
        val id: String,
        val name: String
)