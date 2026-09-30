<KEYWORD>namespace</KEYWORD> module_path.module_name;

<KEYWORD>using</KEYWORD> std.traits.<SYM_TRAIT>PartialEq</SYM_TRAIT>;
<KEYWORD>using</KEYWORD> <KEYWORD>crate</KEYWORD>.{<SYM_CLASS>ExternalClass</SYM_CLASS> <KEYWORD>as</KEYWORD> <SYM_CLASS>Ec</SYM_CLASS>, <SYM_TRAIT>ExternalTrait</SYM_TRAIT> <KEYWORD>as</KEYWORD> <SYM_TRAIT>Et</SYM_TRAIT>};

trait <SYM_TRAIT>TraitName</SYM_TRAIT> {
    def <SYM_FUNCTION_SELF>trait_method</SYM_FUNCTION_SELF>(<SYM_ARG_SELF>self</SYM_ARG_SELF>) -> <KEYWORD>Self</KEYWORD>;
    def <SYM_FUNCTION_SELF>trait_method_mut</SYM_FUNCTION_SELF>(<KEYWORD>mut</KEYWORD> <SYM_ARG_SELF_MUT>self</SYM_ARG_SELF_MUT>) -> <KEYWORD>Self</KEYWORD>;
    def <SYM_FUNCTION_FREE>trait_static</SYM_FUNCTION_FREE>(<SYM_ARG>arg</SYM_ARG> = 0) -> <KEYWORD>Self</KEYWORD>;
    def <SYM_FUNCTION_FREE>trait_static_mut</SYM_FUNCTION_FREE>(<KEYWORD>mut</KEYWORD> <SYM_ARG_MUT>arg</SYM_ARG_MUT>) -> <KEYWORD>Self</KEYWORD>;
}

class <SYM_CLASS>ClassName</SYM_CLASS>: <SYM_TRAIT>PartialEq</SYM_TRAIT> {
    <SYM_FIELD>field_string</SYM_FIELD>: <SYM_CLASS>String</SYM_CLASS> = <STRING>""</STRING>
    <SYM_FIELD>field_bool</SYM_FIELD>: <SYM_CLASS>Boolean</SYM_CLASS> = <KEYWORD>true</KEYWORD>
    <SYM_FIELD>field_int</SYM_FIELD>: <SYM_CLASS>Integer</SYM_CLASS> = 0
    <SYM_FIELD>field_f32</SYM_FIELD>: <SYM_CLASS>Decimal</SYM_CLASS> = 0.0
}

class <SYM_CLASS>ClassTuple</SYM_CLASS>(<SYM_CLASS>Integer</SYM_CLASS> = 0, <SYM_CLASS>Integer</SYM_CLASS> = 0)

<SYM_MACRO>#derive</SYM_MACRO>(<SYM_TRAIT>PartialEq</SYM_TRAIT>)
class <SYM_CLASS>ClassBlock</SYM_CLASS> {
    0: <SYM_CLASS>Integer</SYM_CLASS> = 0,
    1: <SYM_CLASS>Integer</SYM_CLASS> = 0,
}

tagged <SYM_CLASS>TaggedName</SYM_CLASS> {
    <SYM_VARIANT>VariantName</SYM_VARIANT>()
}

bitset <SYM_CLASS>BitsetName</SYM_CLASS>(<KEYWORD>u8</KEYWORD>) {
   <SYM_FIELD>BIT_FIELD</SYM_FIELD> = 0b0000_0001
}

def <SYM_FUNCTION_FREE>is_some</SYM_FUNCTION_FREE>(<SYM_ARG>maybe</SYM_ARG>: <SYM_CLASS>Option</SYM_CLASS><<SYM_GENERIC>T</SYM_GENERIC>>) -> <SYM_CLASS>Boolean</SYM_CLASS> {
    <SYM_ARG>maybe</SYM_ARG> match {
        <KEYWORD>case</KEYWORD> <SYM_VARIANT>Some</SYM_VARIANT>(<SYM_LOCAL>s</SYM_LOCAL>): <KEYWORD>true</KEYWORD>
        <KEYWORD>case</KEYWORD> <SYM_VARIANT>None</SYM_VARIANT>: <KEYWORD>false</KEYWORD>
    }
}
