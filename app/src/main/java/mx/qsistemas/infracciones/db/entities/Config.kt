package mx.qsistemas.infracciones.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class Config(@PrimaryKey(autoGenerate = true) val id: Int,
                  @ColumnInfo(name = "SALARIO_MINIMO") val minimum_salary: Float,
                  @ColumnInfo(name = "PLAZO_DIAS_DESCUENTO_INFRACCION") val discount_days_terms: Int,
                  @ColumnInfo(name = "TAZA_DESCUENTO_INFRACCION_MISMO_DIA") val same_day_discount: Float,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_ENCABEZADO_I") val header_print_1: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_ENCABEZADO_II") val header_print_2: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_ENCABEZADO_III") val header_print_3: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_ENCABEZADO_IV") val header_print_4: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_ENCABEZADO_V") val header_print_5: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_ENCABEZADO_VI") val header_print_6: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_PIE_I") val footer_print_1: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_PIE_II") val footer_print_2: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_PIE_III") val footer_print_3: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_DIR_PAGO_I") val directory_print_1: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_DIR_PAGO_II") val directory_print_2: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_DIR_PAGO_III") val directory_print_3: String,
                  @ColumnInfo(name = "INFRACCION_IMPRESION_DIR_PAGO_IV") val directory_print_4: String,
                  @ColumnInfo(name = "INFRACCION_MUNICIPIO") val infraccion_municipio: String,
                  @ColumnInfo(name = "INFRACCION_ID_PAIS") val id_country: Int,
                  @ColumnInfo(name = "INFRACCION_ID_ENTIDAD") val id_entity: Int,
                  @ColumnInfo(name = "INFRACCION_ID_MUNICIPIO") val id_town: Int
)