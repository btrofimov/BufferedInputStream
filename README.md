BufferedInputStream
===================

Enhanced version of BufferedInputStream without available() usage.
 CustomImplementation of BuffereInputStream which does not use available() of nested stream at all. 
 It might be reasonable and helpful for those InputStreams that does not impement or implement available with errors [yes, it is truth, one very famous IT company privodes API with error inside available(). ].
 This implementation uses O(1) RAM consuming.
