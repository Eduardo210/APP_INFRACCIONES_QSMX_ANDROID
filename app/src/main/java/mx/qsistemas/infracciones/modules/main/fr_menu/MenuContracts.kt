package mx.qsistemas.infracciones.modules.main.fr_menu

import mx.qsistemas.infracciones.net.catalogs.HomeOptions

class MenuContracts {
    interface Presenter {
        fun onHomeOptionsReady(list: MutableList<HomeOptions>)
        fun onDatabaseSend()
        fun onError(msg: String)
    }

    interface Iterator {
        fun getAttributes(): MutableList<Long>
        fun getHomeOptions()
        fun sendDatabase()
        fun checkIfApplicationIsActive(): Boolean
    }

    interface OnHomeOptionListener {
        fun onClickOption(idOption: String)
    }
}