package mx.qsistemas.infracciones

import androidx.room.Room
import androidx.test.runner.AndroidJUnit4
import mx.qsistemas.infracciones.db.AppDatabase
import mx.qsistemas.infracciones.db.dao.PersonAccountDao
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var userDao: PersonAccountDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = Application.getContext()
        db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
        userDao = db.personAccountDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val byName = userDao.selectUserByName("george")
        //assertThat(byName.get(0), equalTo(user))
    }
}