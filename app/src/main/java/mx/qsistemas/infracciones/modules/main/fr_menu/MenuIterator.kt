package mx.qsistemas.infracciones.modules.main.fr_menu

class MenuIterator(val listener: MenuContracts.Presenter): MenuContracts.Iterator {

    override fun getReports() {
        // TODO: Get last reports from local DB in the last 24 hours
    }
}