--# -path=.:prelude:alltenses

concrete Core_Eng of Core_ = Core_I with 

   (Basic  = BasicEng),
   (Syntax = SyntaxEng), 
   (Extra  = ExtraEng) ** open SyntaxEng, ParadigmsEng in { 

  lin owl_Thing = mkPredicate (mkCN (mkN "thing"));

}
