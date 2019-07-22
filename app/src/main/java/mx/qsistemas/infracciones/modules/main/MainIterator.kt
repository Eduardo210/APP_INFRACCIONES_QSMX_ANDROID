package mx.qsistemas.infracciones.modules.main


class MainIterator(private val presenter: MainContracts.Presenter): MainContracts.Iterator {

    override fun closeSession(){
        //Application.prefs?.saveDataBool(R.string.sp_has_session, false)
    }
}