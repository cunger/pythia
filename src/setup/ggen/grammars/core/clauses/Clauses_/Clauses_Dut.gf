--# -path=.:prelude:alltenses

concrete Clauses_Dut of Clauses_ = CoreDut ** Clauses_I with 

   (Basic  = BasicDut),
   (Syntax = SyntaxDut) ** { 

  oper 

     negPol : Pol = negativePol;

   --  mkIWouldLike = overload {
   --  mkIWouldLike : NP -> S = \ np -> ...; -- TODO 
   --  mkIWouldLike : VP -> S = \ vp -> ...; -- TODO
   --  };

}
