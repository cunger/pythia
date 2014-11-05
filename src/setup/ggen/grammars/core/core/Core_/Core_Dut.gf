--# -path=.:prelude:alltenses

concrete Core_Dut of Core_ = Core_I with 

   (Basic  = BasicDut),
   (Syntax = SyntaxDut), 
   (Extra  = ExtraDut) ** open SyntaxDut, ParadigmsDut in {

  lin owl_Thing = mkPredicate (mkCN (mkN "ding"));

}
