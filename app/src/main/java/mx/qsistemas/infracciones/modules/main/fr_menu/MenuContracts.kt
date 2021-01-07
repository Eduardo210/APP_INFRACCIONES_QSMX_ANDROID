package mx.qsistemas.infracciones.modules.main.fr_menu

import mx.qsistemas.infracciones.net.catalogs.HomeOptions

class MenuContracts {
    interface Presenter {
        fun onHomeOptionsReady(list: MutableList<HomeOptions>)
        fun onDatabaseSend()
        fun onError(msg: String)
        fun onApplicationEnable()
        fun onApplicationDisable()
        fun onSessionExpired()
    }

    interface Iterator {
        fun getAttributes(): MutableList<Long>
        fun getHomeOptions()
        fun sendDatabase()
        fun checkIfApplicationIsActive()
        fun validateSession()
        fun closeSession()
    }

    interface OnHomeOptionListener {
        fun onClickOption(idOption: String)
    }
}