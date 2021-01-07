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
        fun onReconfigureFirestore()
    }

    interface Iterator {
        fun getAttributes(): MutableList<String>
        fun getHomeOptions()
        fun sendDatabase()
        fun checkIfApplicationIsActive()
        fun validateSession()
        fun closeSession()
        fun reconfigureDevice(psd: String)
    }

    interface OnHomeOptionListener {
        fun onClickOption(idOption: String)
    }
}