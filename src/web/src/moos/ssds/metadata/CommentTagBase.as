/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (CommentTag.as).
 */

package moos.ssds.metadata {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;

    [Bindable]
    public class CommentTagBase implements IExternalizable, IMetadataObject {

        private var _id:Number;
        private var _tagString:String;
        private var _version:Number;

        public function set id(value:Number):void {
            _id = value;
        }
        public function get id():Number {
            return _id;
        }

        public function set tagString(value:String):void {
            _tagString = value;
        }
        public function get tagString():String {
            return _tagString;
        }

        public function set version(value:Number):void {
            _version = value;
        }
        public function get version():Number {
            return _version;
        }

        public function readExternal(input:IDataInput):void {
            _id = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _tagString = input.readObject() as String;
            _version = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_id);
            output.writeObject(_tagString);
            output.writeObject(_version);
        }
    }
}