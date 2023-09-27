package math;

public class Transform {
	private Matrix4 translationMatrix;
	private Matrix4 translationMatrixInverse;

	/**
	 * I will use the axis angle method + Rodrigues' Rotation Formula to calculate the rotation of the ray/eye point. I will convert the rotation
	 * formula to matrix notation, so I can get the inverse and move back and forth. I pulled the formula for the rotation matrix equivalent from the
	 * Wikipedia article for Rodrigues' Rotation Formula.
	 *
	 * @param incomingLocalSurfaceDirection
	 *            The direction the surface is pointed by default. This helps set up the rotation axis.
	 * @param incomingWorldDirection
	 *            The direction the surface is pointed in world coordinates. This sets up the rotation axis.
	 * @param incomingLocalSurfaceCenter
	 *            The default center of the surface.
	 * @param incomingWorldCenter
	 *            The world center of the surface.
	 * @throws Exception
	 */
	public Transform(
			Vector incomingLocalDirection, Vector incomingWorldDirection, Point incomingWorldCenter) {
		// theta is the angle of rotation; in other words its the degree difference between the local
		// direction
		// and the world direction of the object.
		double sinTheta;
		double cosTheta;
		Vector axis = incomingWorldDirection.cross(incomingLocalDirection);
		cosTheta = incomingWorldDirection.dot(incomingLocalDirection);
		sinTheta = axis.magnitude();
		axis = axis.normalizeReturn();
		// this is how one represents the left side of a cross product vector in matrix form
		Matrix4 crossProductMatrixOfAxis = new Matrix4(
				0.0, -axis.z, axis.y, 0.0, axis.z, 0.0, -axis.x, 0.0, -axis.y, axis.x, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0);
		// this is the wikipedia part...
		Matrix4 leftCrossProductMatrixOfAxis = crossProductMatrixOfAxis.multiplyBy(sinTheta);
		translationMatrix = Matrix4.getIdentity().add(leftCrossProductMatrixOfAxis);

		// right side of equation now
		// this is the second to bottom matrix equation
		Matrix4 axisTimesAxis = crossProductMatrixOfAxis.multiply(crossProductMatrixOfAxis);
		axisTimesAxis = axisTimesAxis.multiplyBy(1 - cosTheta);

		translationMatrix = translationMatrix.add(axisTimesAxis);
		Matrix4 moveMatrix = new Matrix4(
				1.0,
				0.0,
				0.0,
				-incomingWorldCenter.x,
				0.0,
				1.0,
				0.0,
				-incomingWorldCenter.y,
				0.0,
				0.0,
				1.0,
				-incomingWorldCenter.z,
				0.0,
				0.0,
				0.0,
				1.0);
		translationMatrix = translationMatrix.multiply(moveMatrix);
		translationMatrixInverse = translationMatrix.getInverse();
	}

	public Vector translateVectorToLocal(Vector vec) {
		Vector retVec = translationMatrix.multiplyHomogeneousCoordinates(vec);
		retVec = retVec.normalizeReturn();
		return retVec;
	}

	public Point transformPointToLocal(Point p) {
		return translationMatrix.multiplyHomogeneousCoordinates(p);
	}

	public Vector translateVectorToWorld(Vector vec) {
		return translationMatrixInverse.multiplyHomogeneousCoordinates(vec);
	}

	public Point transformPointToWorld(Point p) {
		return translationMatrixInverse.multiplyHomogeneousCoordinates(p);
	}
}
