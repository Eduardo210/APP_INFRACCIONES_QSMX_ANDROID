package mx.qsistemas.infracciones.db_web.entities

class InfractionPrintLocal {
    data class InfractionLocal(
            var date:String="",
            var folio:String="",
            var is_absent:String="",
            var name: String?="",
            var paternal:String?="",
            var maternal:String?="",
            var rfc:String?="",
            var exterior_num:String?="",
            var internal_num:String?="",
            var city_driver_id: String?="",
            var colony_driver_id:String?="",
            var street_driver:String?="",

            //Vehicle
            var year: String?="",
            var colour_id: String?="",
            var class_type_id: String?="", //Sedan, hatchback ...
            var sub_brand_id:String?="",
            var brand_reference: String?="",
            var identifier_document_id: String?="",
            var issued_in_id:String?="",
            var num_document: String?="",//Tarjeta de circulación,  placa, etc...
            var document_type: String?="", //Autoridad Expide


            var street_infra:String?="",
            var street_a: String?="",
            var street_b:String?="",
            var colony_infra_id: String?="",
            var cp_id:String?="",
            var insured_document_id: String?="",
            var third_impound_id:Int=0,
            var town_hall_id: Long=0,
            var employee: String?="",
            var date_capture_line_l :String="",
            var date_capture_line_ll : String="",
            var amount_l : String="",
            var amount_ll : String="",
            var capture_line_l : String="",
            var capture_line_ll: String="",
            var city_infra_id:String?="")
}