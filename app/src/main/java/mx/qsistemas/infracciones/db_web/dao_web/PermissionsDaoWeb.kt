package mx.qsistemas.infracciones.db_web.dao_web

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.qsistemas.infracciones.db_web.entities.Permissions

@Dao
interface PermissionsDaoWeb {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(permission: Permissions)

    @Query("DELETE FROM permissions")
    fun deleteAll()
}