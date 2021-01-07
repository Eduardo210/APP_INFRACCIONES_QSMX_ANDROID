package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.db_web.entities.Permissions
import mx.qsistemas.infracciones.net.result_web.CPermissions
import org.apache.commons.lang.mutable.Mutable

@SuppressLint("StaticFieldLeak")
object PermissionsMgr {
    fun insertPermissions(permissions: MutableList<CPermissions>){
        GlobalScope.launch {
            Application.m_database_web.permissionsDao().deleteAll()
            permissions.forEach { catalog ->
                val permissions = Permissions(0, catalog.name, catalog.code)
                Application.m_database_web.permissionsDao().insert(permissions)
            }
        }
    }

    fun getPermissions(): MutableList<Permissions> = runBlocking {
        return@runBlocking Application.m_database_web.permissionsDao().selectAll()
    }
}