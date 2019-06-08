package mx.qsistemas.payments_transfer.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "soap12:Envelope")
@NamespaceList(*[Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "soap12", reference = "http://www.w3.org/2003/05/soap-envelope")])
class GetKey_Request {
    @field:Element(name = "soap12:Body", required = false)
    var body: GetKey_Body? = null
}

@Root(name = "soap12:Body", strict = false)
class GetKey_Body {
    @field:Element(name = "GetKey", required = false)
    var data: GetKey_BodyData? = null
}

@Root(name = "GetKey", strict = false)
@Namespace(reference = "http://qsistemas.mx/")
class GetKey_BodyData {
    @field:Element(name = "sTerminal", required = false)
    var serialNumber: String? = null

    @field:Element(name = "sAfiliacion", required = false)
    var merchantId: String? = null

    @field:Element(name = "sUsuario", required = false)
    var user: String? = null

    @field:Element(name = "sPassword", required = false)
    var psw: String? = null

    @field:Element(name = "sNumControl", required = false)
    var controlNumber: String? = null
}

@Root(name = "soap12:Envelope")
@NamespaceList(*[Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "soap12", reference = "http://www.w3.org/2003/05/soap-envelope")])
class CipherData_Request {
    @field:Element(name = "soap12:Body", required = false)
    var body: CipherData_Body? = null
}

@Root(name = "soap12:Body", strict = false)
class CipherData_Body {
    @field:Element(name = "Encrypt_KeyStr", required = false)
    var data: CipherData_BodyData? = null
}

@Root(name = "Encrypt_KeyStr", strict = false)
@Namespace(reference = "http:/Walkthrough/XmlWebServices/")
class CipherData_BodyData {
    @field:Element(name = "Param1", required = false)
    var value: String? = null

    @field:Element(name = "Param2", required = false)
    var key: String? = null
}
