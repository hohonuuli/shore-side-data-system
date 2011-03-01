/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package moos.ssds.metadata {
	import flash.utils.IDataInput;
	
	import mx.collections.ListCollectionView;

    [Bindable]
    [RemoteClass(alias="moos.ssds.metadata.DataProducer")]
    public class DataProducer extends DataProducerBase {
		
		// The variable to define the icon for the DataProducer
		public var icon:Class;
		
		// An image that can be used for open deployments
		[Embed(source="/images/tree/tree_CurrentDeployment.gif")]
		private var openDeploymentIcon:Class;
		
		// An image that can be used for close deployments
		[Embed(source="/images/tree/tree_ClosedDeployment.gif")]
		private var closedDeploymentIcon:Class;

		// Override the base class read external
		public override function readExternal(input:IDataInput):void {
			childDataProducers = input.readObject() as ListCollectionView;
			dataProducerGroups = input.readObject() as ListCollectionView;
			dataProducerType = input.readObject() as String;
			// Just read the date range even though it is not there
			input.readObject();
			description = input.readObject() as String;
			device = input.readObject() as Device;
			endDate = input.readObject() as Date;
			if (endDate == null) {
				icon = openDeploymentIcon;
			} else {
				icon = closedDeploymentIcon;
			}
			events = input.readObject() as ListCollectionView;
			hostName = input.readObject() as String;
			id = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			inputs = input.readObject() as ListCollectionView;
			keywords = input.readObject() as ListCollectionView;
			name = input.readObject() as String;
			nominalBenthicAltitude = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalBenthicAltitudeAccuracy = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalDepth = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalDepthAccuracy = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalLatitude = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalLatitudeAccuracy = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalLongitude = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			nominalLongitudeAccuracy = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			orientationDescription = input.readObject() as String;
			outputs = input.readObject() as ListCollectionView;
			parentDataProducer = input.readObject() as DataProducer;
			person = input.readObject() as Person;
			resources = input.readObject() as ListCollectionView;
			role = input.readObject() as String;
			software = input.readObject() as Software;
			startDate = input.readObject() as Date;
			version = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			x3DOrientationText = input.readObject() as String;
			xoffset = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			yoffset = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
			zoffset = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
		}
		
    }
}