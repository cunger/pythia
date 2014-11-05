
abstract Core_Datatypes = Core_ ** {

 cat 

    Unit;
    Measure;

 fun

    ---- Booleans ----

    xsd_boolean : Class;
    xsd_true    : Entity;
    xsd_false   : Entity;

    ---- Strings ----

    xsd_string  : Class;

    ---- Dates and times ----

    xsd_date       : Class;
    xsd_dateTime   : Class;
    xsd_gYear      : Class;
    xsd_gYearMonth : Class;
    
    ---- URIs ----

    xsd_anyURI : Class;    
    

    ---- Numerals ----

    xsd_integer            : Class;
    xsd_positiveInteger    : Class;
    xsd_nonNegativeInteger : Class;
    xsd_double             : Class;
    xsd_float              : Class;
    xsd_decimal            : Class;

    -- conversion of GF types to XSD types --

    value_int   : Int   -> Entity;
    value_float : Float -> Entity;

    value_int_unit   : Int   -> Unit -> Entity;
    value_float_unit : Float -> Unit -> Entity;

    range_int   : Int   -> Int   -> Entity; 
    range_float : Float -> Float -> Entity;   

    range_int_unit   : Int   -> Int   -> Unit -> Entity; 
    range_float_unit : Float -> Float -> Unit -> Entity;   

    -- Measures

    a_few, several : Unit -> Entity;

    -- descriptive : Predicate -> Measure -> Predicate;
    -- Low, Medium, High : Measure;


    -- Numeral determiners --

    at_least_int,        at_most_int        : Int           -> Entity; 
    at_least_int_unit,   at_most_int_unit   : Int   -> Unit -> Entity;

    at_least_float,      at_most_float      : Float         -> Entity; 
    at_least_float_unit, at_most_float_unit : Float -> Unit -> Entity;


    ---- Units (name, both singular and plural, and abbreviation) ----

    Second_name, Second_name_pl, Second_abbr : Unit; 
    Minute_name, Minute_name_pl, Minute_abbr : Unit;
    Hour_name,   Hour_name_pl,   Hour_abbr   : Unit; 
    Day_name,    Day_name_pl,    Day_abbr    : Unit;
    Week_name,   Week_name_pl,   Week_abbr   : Unit; 
    Month_name,  Month_name_pl,  Month_abbr  : Unit;
    Year_name,   Year_name_pl,   Year_abbr   : Unit;

    Meter_name,     Meter_name_pl,     Meter_abbr     : Unit;
    Kilometer_name, Kilometer_name_pl, Kilometer_abbr : Unit;

    Percent_name, Percent_name_pl, Percent_abbr : Unit;

}
