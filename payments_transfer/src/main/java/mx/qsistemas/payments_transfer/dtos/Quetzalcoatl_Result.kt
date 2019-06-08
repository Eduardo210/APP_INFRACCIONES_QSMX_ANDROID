package mx.qsistemas.payments_transfer.dtos

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root

@Root(name = "soap:Envelope")
@NamespaceList(*[Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")])
class GetKey_Result {
    @field:Element(name = "Body", required = false)
    var body: GetKey_Response? = null
}

@Root(name = "soap:Body")
class GetKey_Response {
    @field:Element(name = "GetKeyResponse", required = false)
    var data: GetKey_ResponseData? = null
}

@Root(name = "GetKeyResponse")
@Namespace(reference = "http://qsistemas.mx/")
class GetKey_ResponseData {
    @field:Element(name = "GetKeyResult", required = false)
    var result: String? = null
}

@Root(name = "soap:Envelope")
@NamespaceList(*[Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")])
class CipherData_Result {
    @field:Element(name = "Body", required = false)
    var body: CipherData_Response? = null
}

@Root(name = "soap:Body")
class CipherData_Response {
    @field:Element(name = "Encrypt_KeyStrResponse", required = false)
    var data: CipherData_ResponseData? = null
}

@Root(name = "Encrypt_KeyStrResponse")
@Namespace(reference = "http:/Walkthrough/XmlWebServices/")
class CipherData_ResponseData {
    @field:Element(name = "Encrypt_KeyStrResult", required = false)
    var result: String? = null
}
