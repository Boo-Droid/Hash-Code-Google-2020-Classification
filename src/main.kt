import java.io.File

fun compute(fileName: String) {
    var days: Long = -1
    val libraries: MutableList<Library> = mutableListOf()

    var currentLibrary: Library? = null
    val books: MutableList<Book> = mutableListOf()

    var i = 0
    File("$fileName.txt").forEachLine { line ->
        when (i) {
            0 -> {
                line.split(" ").let {
                    days = it[2].toLong()
                }
            }
            1 -> {
                line.split(" ").forEachIndexed { index, s ->
                    books.add(Book(id = index, score = s.toLong()))
                }
            }
            else -> {
                if (line.isNotEmpty()) {
                    if (i % 2 == 0) {
                        line.split(" ").let {
                            currentLibrary = Library(
                                    id = libraries.size,
                                    timeToSignUp = it[1].toLong(),
                                    booksPerDay = it[2].toLong()
                            )
                            libraries.add(currentLibrary!!)
                        }
                    } else {
                        line.split(" ").forEach {
                            currentLibrary?.books?.add(books[it.toInt()])
                        }
                    }
                }
            }
        }
        i++
    }

    libraries.sortBy {
        it.totalScore()
    }

    libraries.forEach {
        it.books.sortByDescending { book ->
            book.score
        }
    }

    val librariesToVisit: MutableList<Library> = mutableListOf()
    var totalDaysSignUp = 0L
    var index = 0
    while (totalDaysSignUp <= days && index < libraries.size) {
        val library = libraries[index]
        librariesToVisit.add(library)
        totalDaysSignUp += library.timeToSignUp
        index++
    }

    if (totalDaysSignUp > days) {
        librariesToVisit.removeAt(librariesToVisit.size - 1)
    }

    var result = "${librariesToVisit.size}\n"

    val newBooks = mutableListOf<Book>()
    var currentDay = 0L
    librariesToVisit.forEach { library ->
        library.deleteExistBooks(newBooks)

        val booksToShip = library.booksToShip(currentDay = currentDay, finalDay = days)
        currentDay += library.timeToSignUp

        val anotherBooks = mutableListOf<Book>()

        if (booksToShip > 0) {
            result += "${library.id} $booksToShip\n"
            (0 until booksToShip).forEach {
                result += "${library.books[it.toInt()].id} "
                anotherBooks.add(library.books[it.toInt()])
            }
            newBooks.addAll(anotherBooks)
            result += "\n"
        }
    }

    File("$fileName.out").writeText(result)
}

fun main() {
    listOf("a_example", "b_read_on", "c_incunabula", "d_tough_choices", "e_so_many_books", "f_libraries_of_the_world").forEach {
        compute(fileName = it)
    }
}