package mx.qsistemas.payments_transfer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.StrictMode
import android.util.Log
import com.basewin.aidl.OnBarcodeCallBack
import com.basewin.aidl.OnPrinterListener
import com.basewin.define.BwLedType
import com.basewin.define.InputPBOCInitData
import com.basewin.define.TransactionType
import com.basewin.log.LogUtil
import com.basewin.services.ServiceManager
import com.basewin.utils.BCDHelper
import com.basewin.utils.LoadParamManage
import com.facebook.stetho.Stetho
import com.google.gson.Gson
import mx.qsistemas.payments_transfer.db.PaymentsDatabase
import mx.qsistemas.payments_transfer.db.manager.ReversalManager
import mx.qsistemas.payments_transfer.dtos.*
import mx.qsistemas.payments_transfer.net.API_Quetzalcoatl
import mx.qsistemas.payments_transfer.pboc.PbocListener
import mx.qsistemas.payments_transfer.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection


//const val CONTROL_NUMBER_TOWNSHIP = "M"
const val CONTROL_NUMBER_TERMINAL = "T"
const val CONTROL_NUMBER_ID = "ID"
const val TRANSACTION_TYPE = "Venta"

@SuppressLint("StaticFieldLeak")
object PaymentsTransfer : Interfaces.Contracts {

    private lateinit var paymentsPreferences: Preferences
    private lateinit var paymentsContext: Context
    private lateinit var txListener: IPaymentsTransfer.TransactionListener
    private lateinit var amount: String
    private lateinit var activity: Activity
    internal var database: PaymentsDatabase? = null
    lateinit var modeTx: String
    var isDeviceConfigured: Boolean = false
    private val aid_data = arrayOf(
            "9F0607A0000000250000DF0101009F08020020DF11050000000000DF12050000000000DF130500000000009F1B0400000000DF150400000000DF160100DF170100DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0605A000000025DF0101009F08020020DF11050000000000DF12050000000000DF130500000000009F1B0400000000DF150400000000DF160100DF170100DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0606A00000002501DF0101009F08020020DF11050000000000DF12050000000000DF130500000000009F1B0400000000DF150400000000DF160100DF170100DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000000031010DF0101009F0802008CDF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000000032010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000000033010DF0101009F08020140DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000000041010DF0101009F08020002DF1105FC5080A000DF1205F85080F800DF130504000000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000000043060DF0101009F08020002DF1105DC4000A800DF1205DC4004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000000651010DF0101009F08020200DF1105FC6024A800DF1205FC60ACF800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000003330101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000007241010DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000007242010DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06002147483647DF1906002147483647DF2006002147483647DF2106002147483647",
            "9F0607A0000005241010DF0101009F08020002DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000000000DF1906000000000000DF2006002147483647DF2106000000001000",
            "9F0607A0000005241011DF0101009F08020002DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000000000DF1906000000000000DF2006002147483647DF2106000000001000",
            "9F0605A000000152DF0101009F08020002DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000000000DF1906000000000000DF2006002147483647DF2106000000001000"
    )
    private val rid_data = arrayOf(
            "9F0605A0000000659F220109DF05083230353031323331DF060101DF070101DF028180B72A8FEF5B27F2B550398FDCC256F714BAD497FF56094B7408328CB626AA6F0E6A9DF8388EB9887BC930170BCC1213E90FC070D52C8DCD0FF9E10FAD36801FE93FC998A721705091F18BC7C98241CADC15A2B9DA7FB963142C0AB640D5D0135E77EBAE95AF1B4FEFADCF9C012366BDDA0455C1564A68810D7127676D493890BDDF040103DF03144410C6D51C2F83ADFD92528FA6E38A32DF048D0A",
            "9F0605A0000000659F220110DF05083230353031323331DF060101DF070101DF02819099B63464EE0B4957E4FD23BF923D12B61469B8FFF8814346B2ED6A780F8988EA9CF0433BC1E655F05EFA66D0C98098F25B659D7A25B8478A36E489760D071F54CDF7416948ED733D816349DA2AADDA227EE45936203CBF628CD033AABA5E5A6E4AE37FBACB4611B4113ED427529C636F6C3304F8ABDD6D9AD660516AE87F7F2DDF1D2FA44C164727E56BBC9BA23C0285DF040103DF0314C75E5210CBE6E8F0594A0F1911B07418CADB5BAB",
            "9F0605A0000000659F220112DF05083230353031323331DF060101DF070101DF0281B0ADF05CD4C5B490B087C3467B0F3043750438848461288BFEFD6198DD576DC3AD7A7CFA07DBA128C247A8EAB30DC3A30B02FCD7F1C8167965463626FEFF8AB1AA61A4B9AEF09EE12B009842A1ABA01ADB4A2B170668781EC92B60F605FD12B2B2A6F1FE734BE510F60DC5D189E401451B62B4E06851EC20EBFF4522AACC2E9CDC89BC5D8CDE5D633CFD77220FF6BBD4A9B441473CC3C6FEFC8D13E57C3DE97E1269FA19F655215B23563ED1D1860D8681DF040103DF0314874B379B7F607DC1CAF87A19E400B6A9E25163E8",
            "9F0605A0000000659F220114DF05083230353031323331DF060101DF070101DF0281F8AEED55B9EE00E1ECEB045F61D2DA9A66AB637B43FB5CDBDB22A2FBB25BE061E937E38244EE5132F530144A3F268907D8FD648863F5A96FED7E42089E93457ADC0E1BC89C58A0DB72675FBC47FEE9FF33C16ADE6D341936B06B6A6F5EF6F66A4EDD981DF75DA8399C3053F430ECA342437C23AF423A211AC9F58EAF09B0F837DE9D86C7109DB1646561AA5AF0289AF5514AC64BC2D9D36A179BB8A7971E2BFA03A9E4B847FD3D63524D43A0E8003547B94A8A75E519DF3177D0A60BC0B4BAB1EA59A2CBB4D2D62354E926E9C7D3BE4181E81BA60F8285A896D17DA8C3242481B6C405769A39D547C74ED9FF95A70A796046B5EFF36682DC29DF040103DF0314C0D15F6CD957E491DB56DCDD1CA87A03EBE06B7B",
            "9F0605A0000000039F220101DF05083230353031323331DF060101DF070101DF028180C696034213D7D8546984579D1D0F0EA519CFF8DEFFC429354CF3A871A6F7183F1228DA5C7470C055387100CB935A712C4E2864DF5D64BA93FE7E63E71F25B1E5F5298575EBE1C63AA617706917911DC2A75AC28B251C7EF40F2365912490B939BCA2124A30A28F54402C34AECA331AB67E1E79B285DD5771B5D9FF79EA630B75DF040103DF0314D34A6A776011C7E7CE3AEC5F03AD2F8CFC5503CC",
            "9F0605A0000000039F220107DF05083230353031323331DF060101DF070101DF028190A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725FDF040103DF0314B4BC56CC4E88324932CBC643D6898F6FE593B172",
            "9F0605A0000000039F220108DF05083230353031323331DF060101DF070101DF0281B0D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9CF35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC2603445469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE38F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55A5A9218ACE0F7A21287752682F15832A678D6E1ED0BDF040103DF031420D213126955DE205ADC2FD2822BD22DE21CF9A8",
            "9F0605A0000000039F220109DF05083230353031323331DF060101DF070101DF0281F89D912248DE0A4E39C1A7DDE3F6D2588992C1A4095AFBD1824D1BA74847F2BC4926D2EFD904B4B54954CD189A54C5D1179654F8F9B0D2AB5F0357EB642FEDA95D3912C6576945FAB897E7062CAA44A4AA06B8FE6E3DBA18AF6AE3738E30429EE9BE03427C9D64F695FA8CAB4BFE376853EA34AD1D76BFCAD15908C077FFE6DC5521ECEF5D278A96E26F57359FFAEDA19434B937F1AD999DC5C41EB11935B44C18100E857F431A4A5A6BB65114F174C2D7B59FDF237D6BB1DD0916E644D709DED56481477C75D95CDD68254615F7740EC07F330AC5D67BCD75BF23D28A140826C026DBDE971A37CD3EF9B8DF644AC385010501EFC6509D7A41DF040103DF03141FF80A40173F52D7D27E0F26A146A1C8CCB29046",
            "9F0605A0000000039F220192DF050420291231DF0281B0996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9FDF040103DF0314429C954A3859CEF91295F663C963E582ED6EB253BF010131DF070101",
            "9F0605A0000000049F220103DF05083230353031323331DF060101DF070101DF028180C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B60145DF040103DF03145ADDF21D09278661141179CBEFF272EA384B13BB",
            "9F0605A0000000049F220104DF05083230353031323331DF060101DF070101DF028190A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5DF040103DF0314381A035DA58B482EE2AF75F4C3F2CA469BA4AA6C",
            "9F0605A0000000049F220105DF05083230353031323331DF060101DF070101DF0281B0B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED597DF040103DF0314EBFA0D5D06D8CE702DA3EAE890701D45E274C845",
            "9F0605A0000000049F220106DF05083230353031323331DF060101DF070101DF0281F8CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747FDF040103DF0314F910A1504D5FFB793D94F3B500765E1ABCAD72D9",
            "9F0605A0000007249F220101DF05083230353031323331DF060101DF070101DF0281B0AE0B89755F0509F111FDF7CDBABE0491A2E3A6A778A0FCB1744C5445749FE9407E5BDE86D402DC63BAE999BD6698132181BE2AAD0B96C9BEBA11A521B165165AA40057292F79F7329724D178AF18FC342BB8B58D1DD84FF44847056BF17F66307500228558D847678F9FA462E290F3DFD898F11381BA1710B94D42F160780D0F60A909516653978AE750568B3960071092633530C053FFB7097EFBF140AFCB196861A0DA94ECDAC8D336BE97B8E9AFB7DF040103DF0314602A4CAB6084C493F01B29AB41F3140B85EABBEF",
            "9F0605A0000007249F220102DF05083230353031323331DF060101DF070101DF0281F8AABB504E3B2CC61F243A6BE3BFB18B649E17ED625F1F8EE04704873AD60564B22A1B0258EA1D25BA7A19F53D59E5FC60E85CE1535F99774ACA94A636F430F0A0E7E98BD5738475C66EBFED3FF4220EF8347D203BF3640714D47132AAC8276145DBA29A055CFE1476CF8CB0AD90A4FFA76F58A8CD2433B802829CD7E49AB10123B83E8BA1E90D556607DF128F7342467230E30B8C347137CD106E212D30E9BD5867D7232B425B3F644F02D71FED7A3C3CCA67BE4C04BC711C4B32DF8F7194765C65566B2E27CB86DEB49EA0E2F65B80D54DBE923C421027413BAA3B93517EE08631D58D00098BA22372921321C40CC5115346587ED436A181DF040103DF031415C48DAD19DB502AB397647A38A0755E0FD0CB39",
            "9F0605A0000007249F2201C1DF05083230353031323331DF060101DF070101DF0281B0C71B645381635A8887E4FF56C866EA5F6E4BBE1C1CA0A520EB1DFB94150B50EA36688593F7F6F5189E747F22454CA6421EE83B334F39C99F598053194AE5B155CD8DB5FE9B3A86DE5F4162A273BDB8FD1A0314DDD315665B3DAD51F24FDFDAA66FF49B3081906CDC1C5AF7AFE7C4D0F0212250C5C6FA4159B2C937B3ED00B5E5524796F06CF79CE191FFDC0A6E6D7004CAFDCA1B8BDAF2DF32DB86DE45CD7FB477607419E9C12E29BADCA9FB7011DAFFDF040103DF0314655056A49191FA5BE0420D40985CAD67319FEE24",
            "9F0605A0000007249F2201C2DF05083230353031323331DF060101DF070101DF0281F891E3215B117D300196F5A169F6511B63696991EAA6E2A6243C60E511A5CACF6CBBE02069AD4BF51037F89A02793DB5EC5FBAC5133AFF2A804EDA692BE6B6973907597D7E8B5847C498FBFCE711C5C2513370172426D43E88C1C9DDA11EC292F1FD3BCED0175E729661952A9AE7053F7DEE34ED39C630747ED3ED2A5ADA6ED5B81505697B2189623CFA242EC0DD6DEC01FD5831F443625C57976E95CCB4C7B3616DBB914BB6E935498CB1C54DFA3E8DCDB65BD5004762A8B7B6F88FE42BA0D42A790DA400A433407A3C6B9680837BB3A7529E54F13D2DB8D4006885700C3A48762BA39B2DC903962348E13E06894285A61E6BF03AC6F8C5DFDF040103DF0314A664B92ACDA5130F444A584AF72201664EFBC9F6"
    )

    override fun initialize(context: Context) {
        paymentsContext = context
        /* Granted permission to access to storage*/
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        /* Init Device Server */
        ServiceManager.getInstence().init(context)
        /* Init the GlobalData cache         */
        GlobalData.getInstance().init(context)
        /* Init shared preferences of the library */
        paymentsPreferences = Preferences(paymentsContext)
        database = PaymentsDatabase.getInMemoryDatabase(paymentsContext)
        isDeviceConfigured = paymentsPreferences.loadDataBoolean(R.string.pt_sp_quetz_is_device_config, false)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(paymentsContext)
            LogUtil.openLog()
        }
    }

    override fun configDevice(prefix: String, terminalCode: String) {
        val AREA = 2
        val TMKID = 1
        /* Save the control number of initialize */
        paymentsPreferences.saveData(R.string.pt_sp_quetz_prefix, prefix)
        paymentsPreferences.saveData(R.string.pt_sp_quetz_id_terminal, terminalCode)
        val keyValue1 = "13F0A294679546195AD092C4E5937A41"
        val keyValue2 = Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(0, 32)
        val keyValue3 = Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(0, 32)
        val checkValue1 = Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(120)
        val checkValue2 = Utils.Sha512Hex(System.currentTimeMillis().toString()).toUpperCase().substring(120)
        AsyncTask.execute {
            /* Load Params of the terminal */
            ServiceManager.getInstence().pinpad.loadProtectKeyByArea(AREA, keyValue1)
            GlobalData.getInstance().pinpadVersion = PINPAD_INTERFACE_VERSION3
            GlobalData.getInstance().area = AREA
            ServiceManager.getInstence().pinpad.loadMainKeyByArea(AREA, TMKID, keyValue2)
            //ServiceManager.getInstence().pinpad.loadMainKeyWithKcvByArea(AREA, TMKID, keyValue2, checkValue1)
            GlobalData.getInstance().tmkId = TMKID
            ServiceManager.getInstence().pinpad.loadMacKeyByArea(AREA, TMKID, keyValue3, null)
            ServiceManager.getInstence().pinpad.loadPinKeyByArea(AREA, TMKID, keyValue2, null)
            GlobalData.getInstance().pinkeyFlag = true
            ServiceManager.getInstence().pinpad.loadTDKeyByArea(AREA, TMKID, keyValue3, null)
            /* Set time to terminal */
            ServiceManager.getInstence().deviceinfo.spTime = BCDHelper.StrToBCD(System.currentTimeMillis().toString())
            ServiceManager.getInstence().pboc.setEmvParamSetBySdk(false)
            LoadParamManage.getInstance().DeleteAllTerParamFile()
            for (j in aid_data.indices) {
                ServiceManager.getInstence().pboc.updateAID(0, aid_data[j])
            }
            for (i in rid_data.indices) {
                ServiceManager.getInstence().pboc.updateRID(0, rid_data[i])
            }
            var posEmvParam = ServiceManager.getInstence().pboc.posTermPara
            System.arraycopy(byteArrayOf(0x04, 132.toByte()), 0, posEmvParam.TransCurrCode, 0, 2)
            System.arraycopy(byteArrayOf(0x04, 132.toByte()), 0, posEmvParam.CountryCode, 0, 2)
            posEmvParam.TerminalType =
                    0x22  // https://www.emvco.com/wp-content/uploads/2017/05/EMV_v4.3_Book_4_Other_Interfaces_20120607062305603.pdf (Pag. 129)
            posEmvParam.Capability[0] = 0xe0.toByte()
            posEmvParam.Capability[1] = 0xb8.toByte()
            posEmvParam.Capability[2] = 0xc8.toByte()
            ServiceManager.getInstence().pboc.posTermPara = posEmvParam
            posEmvParam = ServiceManager.getInstence().pboc.posTermPara
            if (BuildConfig.DEBUG) {
                Log.i(this.javaClass.simpleName, "posEmvParam:$posEmvParam")
            }
            GlobalData.getInstance().login = true
        }
        paymentsPreferences.saveDataBool(R.string.pt_sp_quetz_is_device_config, true)
        paymentsPreferences.saveDataInt(R.string.pt_sp_quetz_version, BuildConfig.VERSION_CODE)
    }

    override fun reconfigure() {
        Executors.newSingleThreadExecutor().execute {
            ServiceManager.getInstence().pboc.setEmvParamSetBySdk(false)
            LoadParamManage.getInstance().DeleteAllTerParamFile()
            var posEmvParam = ServiceManager.getInstence().pboc.posTermPara
            System.arraycopy(byteArrayOf(0x04, 132.toByte()), 0, posEmvParam.TransCurrCode, 0, 2)
            System.arraycopy(byteArrayOf(0x04, 132.toByte()), 0, posEmvParam.CountryCode, 0, 2)
            posEmvParam.TerminalType =
                    0x22  // https://www.emvco.com/wp-content/uploads/2017/05/EMV_v4.3_Book_4_Other_Interfaces_20120607062305603.pdf (Pag. 129)
            posEmvParam.Capability[0] = 0xe0.toByte()
            posEmvParam.Capability[1] = 0xb8.toByte()
            posEmvParam.Capability[2] = 0xc8.toByte()
            ServiceManager.getInstence().pboc.posTermPara = posEmvParam
            for (j in aid_data.indices) {
                ServiceManager.getInstence().pboc.updateAID(0, aid_data[j])
            }
            for (i in rid_data.indices) {
                ServiceManager.getInstence().pboc.updateRID(0, rid_data[i])
            }
        }
    }

    override fun loadKeyDevice(activity: Activity, data: LoadKeyData, listener: IPaymentsTransfer.LoadKeyListener) {
        activity.runOnUiThread {
            DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.pt_t_obtaining_key))
        }
        if (paymentsPreferences.loadData(R.string.pt_sp_banorte_aes_key, "") == "") {
            val request = GetKey_Request()
            request.body = GetKey_Body()
            request.body?.data = GetKey_BodyData()
            request.body?.data?.serialNumber = data.serialNumber
            request.body?.data?.merchantId = data.merchantId
            request.body?.data?.user = data.user
            request.body?.data?.psw = data.psw
            val idTownship = paymentsPreferences.loadData(R.string.pt_sp_quetz_prefix, "")
            val terminalCode = paymentsPreferences.loadData(R.string.pt_sp_quetz_id_terminal, "")
            var controlCounter = paymentsPreferences.loadDataInt(R.string.pt_sp_banorte_counter_control) + 1
            paymentsPreferences.saveDataInt(R.string.pt_sp_banorte_counter_control, controlCounter)
            request.body?.data?.controlNumber =/*
                    CONTROL_NUMBER_TOWNSHIP + */idTownship + CONTROL_NUMBER_TERMINAL + terminalCode + CONTROL_NUMBER_ID + controlCounter.toString()
            API_Quetzalcoatl.getQuetzalcoatlService().getKey(request).enqueue(object : Callback<GetKey_Result> {
                override fun onResponse(call: Call<GetKey_Result>, response: Response<GetKey_Result>) {
                    if (response.code() == HttpsURLConnection.HTTP_OK && response.body()?.body != null) {
                        paymentsPreferences.saveData(R.string.pt_sp_banorte_aes_key, response.body()?.body?.data?.result!!)
                        paymentsPreferences.saveData(R.string.pt_sp_banorte_serial_number, data.serialNumber)
                        paymentsPreferences.saveData(R.string.pt_sp_banorte_merchant_id, data.merchantId)
                        paymentsPreferences.saveData(R.string.pt_sp_banorte_user, data.user)
                        paymentsPreferences.saveData(R.string.pt_sp_banorte_black_box, data.psw)
                        DialogStatusHelper.closeDialog()
                        listener.onLoadKey(true, paymentsContext.getString(R.string.pt_s_load_key))
                    } else {
                        DialogStatusHelper.closeDialog()
                        listener.onLoadKey(false, paymentsContext.getString(R.string.pt_e_no_response))
                    }
                }

                override fun onFailure(call: Call<GetKey_Result>, t: Throwable) {
                    DialogStatusHelper.closeDialog()
                    listener.onLoadKey(false, t.message
                            ?: paymentsContext.getString(R.string.pt_e_unknown))
                }
            })
        } else {
            DialogStatusHelper.closeDialog()
            listener.onLoadKey(true, paymentsContext.getString(R.string.pt_s_load_key2))
        }
    }

    override fun runTransaction(activity: Activity, amount: String, modeTx: String, listener: IPaymentsTransfer.TransactionListener) {
        this.activity = activity
        this.modeTx = modeTx
        txListener = listener
        this.amount = amount.replace(",", ".")
        activity.runOnUiThread {
            if (this.amount.toFloat() > 400)
                DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.pt_t_waiting_card))
            else
                DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.pt_t_waiting_card2))
        }
        if (paymentsPreferences.loadData(R.string.pt_sp_banorte_aes_key, "") != "") {
            val intent = Intent()
            intent.putExtra(InputPBOCInitData.AMOUNT_FLAG, formatAmount(this.amount))
            intent.putExtra(InputPBOCInitData.IS_PBOC_FORCE_ONLINE, false)
            intent.putExtra(InputPBOCInitData.USE_DEVICE_FLAG, if (this.amount.toFloat() > 400)
                InputPBOCInitData.USE_MAG_CARD or InputPBOCInitData.USE_IC_CARD
            else
                InputPBOCInitData.USE_MAG_CARD or InputPBOCInitData.USE_IC_CARD or InputPBOCInitData.USE_RF_CARD
            )
            intent.putExtra(InputPBOCInitData.TIMEOUT, 60)
            ServiceManager.getInstence().pboc.startTransfer(
                    TransactionType.ONLINE_PAY,
                    intent,
                    PbocListener(this.amount, activity, txListener)
            )
            /* Enable Lights For NFC */
            if (this.amount.toFloat() <= 400) {
                //ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_BLUE, 500, 500)
                ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_GREEN, 500, 500)
                //ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_RED, 500, 500)
                //ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_YELLOW, 500, 500)
            }
        } else {
            DialogStatusHelper.closeDialog()
            try {
                //ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_BLUE, false)
                ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_GREEN, false)
                //ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_RED, false)
                //ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_YELLOW, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            txListener.onTxFailed(false, paymentsContext.getString(R.string.pt_e_need_to_load_key))
        }
    }

    override fun runReversal(activity: Activity) {
        val reversalList = ReversalManager.getTransactionsToReverse()
        this.modeTx = /*if (BuildConfig.DEBUG) MODE_TX_PROBE_AUTH_ALWAYS else*/ MODE_TX_PROD
        reversalList.forEach {
            PbocListener("", activity, object : IPaymentsTransfer.TransactionListener {
                override fun onTxApproved(txInfo: TransactionInfo) {
                }

                override fun onTxFailed(retry: Boolean, message: String) {
                }

                override fun onTxVoucherFailed(message: String) {
                }

                override fun onCtlsDoubleTap() {
                }

                override fun onTxVoucherPrinted() {
                }
            }).processTx(TX_REVERSAL,
                    it.entryMode, it.track1, it.track2, it.emvTags, it.maskedPan, it.nombreTarjetahabiente,
                    it.needSign, it.aid, it.arqc, it.tvr, it.tsi, it.apn, it.al, it.reference, false, it.noControl
            )
        }
    }

    override fun print(activity: Activity, json: String, bitmap: Array<Bitmap>?, listener: IPaymentsTransfer.PrintListener) {
        ServiceManager.getInstence().printer.setPrintGray(1000)
        ServiceManager.getInstence().printer.setPrintFontByAsserts("catamaran_medium.ttf")
        ServiceManager.getInstence().printer.print(json, bitmap, object : OnPrinterListener {
            override fun onStart() {
                activity.runOnUiThread {
                    DialogStatusHelper.showDialog(activity, paymentsContext.getString(R.string.pt_t_printing))
                }
                listener.onStart()
            }

            override fun onError(p0: Int, p1: String?) {
                DialogStatusHelper.closeDialog()
                listener.onError(p0, p1 ?: "")
            }

            override fun onFinish() {
                DialogStatusHelper.closeDialog()
                listener.onFinish()
            }
        })
    }

    override fun scanCode(activity: Activity, timeout: Long, listener: IPaymentsTransfer.ScanCodeListener) {
        ServiceManager.getInstence().scan.startScan(timeout, object : OnBarcodeCallBack {
            override fun onScanResult(p0: String?) {
                listener.onSuccess(p0 ?: "")
            }

            override fun onFinish(p0: Int, p1: String?) {
                listener.onError(p0, p1 ?: "")
            }
        })
    }

    override fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener, voucher: Voucher) {
        val paymentsVoucher = PaymentsVoucher(activity, listener)
        paymentsVoucher.printVoucher(activity, voucher, false)
    }

    override fun printLastVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener) {
        val json = paymentsPreferences.loadData(R.string.pt_sp_last_transaction, "")!!
        if (json.isNotEmpty()) {
            val voucher = Gson().fromJson(json, Voucher::class.java)
            PaymentsVoucher(activity, listener).printVoucher(activity, voucher, false)
        } else {
            try {
                ServiceManager.getInstence().led.enableLedIndex(BwLedType.LED_BLUE, false)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            listener.onTxFailed(false, activity.getString(R.string.pt_e_voucher_not_found))
        }
    }

    private fun formatAmount(amount: String): Int? {
        return Integer.parseInt(StringHelper.changeAmout(amount).replace(".", "").replace(",", ""))
    }
}


internal class Interfaces {
    interface Contracts {
        fun initialize(context: Context)
        fun configDevice(prefix: String, terminalCode: String)
        fun reconfigure()
        fun loadKeyDevice(activity: Activity, data: LoadKeyData, listener: IPaymentsTransfer.LoadKeyListener)
        fun runTransaction(
                activity: Activity,
                amount: String,
                modeTx: String,
                listener: IPaymentsTransfer.TransactionListener
        )

        fun runReversal(activity: Activity)
        fun print(activity: Activity, json: String, bitmap: Array<Bitmap>?, listener: IPaymentsTransfer.PrintListener)
        fun scanCode(activity: Activity, timeout: Long, listener: IPaymentsTransfer.ScanCodeListener)
        fun reprintVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener, voucher: Voucher)
        fun printLastVoucher(activity: Activity, listener: IPaymentsTransfer.TransactionListener)
    }

    interface CipherDataListener {
        fun onCipherData(success: Boolean, value: String)
    }

    interface OnPinDialogListener {
        fun OnPinInput(result: Int)
        fun OnCreateOver()
    }
}