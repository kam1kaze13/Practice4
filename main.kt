data class Subject(
	val title: String, val grade: Int
)

data class Student(
    val name: String, val dateBirth: Int, val subject: List<Subject>
    ){
    val age=2021-dateBirth
    val averageGrade: Float
    get() = subject.average{ it.grade.toFloat() }
}

fun <T> Iterable<T>.average(block: (T) -> Float): Float {
    var sum: Double = 0.0
    var count: Int = 0
    for (element in this){
        sum += block(element)
        ++count
    }
    return (sum/count).toFloat()
}

data class University(
    val title: String, val students: MutableList<Student>
    ) {
    val averageGrade: Float
    get() = students.average{ it.averageGrade }

    val courses: Map<Int, List<Student>>
    get() = students
    	.groupBy { it.age }
    	.mapKeys {
        when (it.key) {
        17 -> 1
        18 -> 2
        19 -> 3
        20 -> 4
        21 -> 5
        else -> throw StudentTooYoungException("Error: Student too young")
        }
    }
}

class StudentTooYoungException(message:String): Exception(message)

enum class StudyProgram(val title: String){
    WEB("web application development"),
    TEST("software testing"),
    NET("neural networks");

    infix fun withGrade(grade: Int) = Subject(title,grade)
}

typealias StudentsListener = (Student) -> Unit

val students = mutableListOf(
Student("Alexey",2003,listOf(StudyProgram.WEB withGrade 4, StudyProgram.TEST withGrade 3, StudyProgram.NET withGrade 5)),
Student("Ivan",2002,listOf(StudyProgram.WEB withGrade 5, StudyProgram.TEST withGrade 3, StudyProgram.NET withGrade 5)),
Student("Michael",2001,listOf(StudyProgram.WEB withGrade 4, StudyProgram.TEST withGrade 4, StudyProgram.NET withGrade 5)),
Student("Petr",2000,listOf(StudyProgram.WEB withGrade 4, StudyProgram.TEST withGrade 4, StudyProgram.NET withGrade 4)),
Student("Maxim",2002,listOf(StudyProgram.WEB withGrade 5, StudyProgram.TEST withGrade 5, StudyProgram.NET withGrade 5)),
Student("Artur",2003,listOf(StudyProgram.WEB withGrade 4, StudyProgram.TEST withGrade 5, StudyProgram.NET withGrade 4)),
Student("Pavel",2001,listOf(StudyProgram.WEB withGrade 5, StudyProgram.TEST withGrade 5, StudyProgram.NET withGrade 4))
)

object DataSource {
	val university : University by lazy {
	University("aaaa", students)
	}

	var onNewStudentListener : StudentsListener ?= null

	fun addStudent(name: String, birthYear: Int, students: MutableList<Student>){
        students.add(Student(name,birthYear,listOf(StudyProgram.WEB withGrade 4,StudyProgram.TEST withGrade 5)))
        val addedStudent = students.last()
        onNewStudentListener?.invoke(addedStudent)
    }
}

fun main() {
    DataSource.onNewStudentListener = {
        println("Новый студент: $it " + "Средняя оценка по университету ${DataSource.university.averageGrade}")
    }
    println(DataSource.university.averageGrade)
    println(DataSource.university.courses)
    DataSource.addStudent("Vladimir", 2002, students)
}