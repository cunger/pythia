--# -path=.:prelude:alltenses

concrete Core_AnaphoraDut of Core_Anaphora = Core_Dut ** Core_AnaphoraI with (Basic = BasicDut), (Syntax = SyntaxDut);