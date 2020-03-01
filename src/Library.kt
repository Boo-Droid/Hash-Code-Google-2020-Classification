import kotlin.math.min

data class Library(
        val id: Int,
        val books: MutableList<Book> = mutableListOf(),
        val timeToSignUp: Long,
        val booksPerDay: Long
) {
    fun totalScore(): Long {
        var score = 0L
        books.forEach {
            score += it.score
        }
        return score
    }

    fun booksToShip(currentDay: Long, finalDay: Long): Long {
        val totalDays = finalDay - currentDay
        val daysToShip = totalDays - timeToSignUp
        return min(daysToShip * booksPerDay, books.size.toLong())
    }

    fun deleteExistBooks(externalBooks: List<Book>) {
        externalBooks.forEach {
            books.remove(it)
        }
    }
}