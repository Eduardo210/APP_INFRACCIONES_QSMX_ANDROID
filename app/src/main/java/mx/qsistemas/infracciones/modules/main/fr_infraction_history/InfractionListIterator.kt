package mx.qsistemas.infracciones.modules.main.fr_infraction_history

class InfractionListIterator(val listener: InfractionListContracts.Presenter): InfractionListContracts.Iterator {

    override fun getReports() {
        // TODO: Get last reports from local DB in the last 24 hours
    }
}