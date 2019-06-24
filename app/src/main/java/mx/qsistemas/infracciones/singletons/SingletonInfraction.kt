package mx.qsistemas.infracciones.singletons

import mx.qsistemas.infracciones.db.entities.Articles
import mx.qsistemas.infracciones.db.entities.InfractionFraction

object SingletonInfraction {
    var motivationList: MutableList<DtoMotivation> = mutableListOf()

    class DtoMotivation(var article: Articles, var fraction: InfractionFraction, var motivation: String)
}