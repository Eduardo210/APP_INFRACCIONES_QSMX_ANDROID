package mx.qsistemas.infracciones.db.dao

import androidx.room.*
import mx.qsistemas.infracciones.db.entities.PersonAccount

@Dao
interface PersonAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: MutableList<PersonAccount>)

    @Query("SELECT DISTINCT pt.ID_PERSONA_AYUNTAMIENTO, pa.USER_NAME, pa.PASSWORD, p.NOMBRE, p.A_PATERNO, p.A_MATERNO, p.id FROM person p INNER JOIN persona_account pa ON p.id = pa.ID_PERSONA INNER JOIN person_towship pt ON p.id = pt.ID_PERSONA WHERE pa.USER_NAME = :userName")
    fun selectUserByName(userName: String): LogInUser

    data class LogInUser(@ColumnInfo(name = "ID_PERSONA_AYUNTAMIENTO") var idPersonTownship: Int,
                         @ColumnInfo(name = "USER_NAME") var userName: String,
                         @ColumnInfo(name = "PASSWORD") var password: String,
                         @ColumnInfo(name = "NOMBRE") var name: String,
                         @ColumnInfo(name = "A_PATERNO") var fLastName: String,
                         @ColumnInfo(name = "A_MATERNO") var mLastName: String,
                         @ColumnInfo(name = "id") var idPerson: Int)
}