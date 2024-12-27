package id.hanifalfaqih.reuseit.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val emailVerifiedAt: String,
    val password: String,
    val role: Int,
    val rememberToken: String,
    val createdAt: String,
    val updatedAt: String
)
