function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%

%
% ******************
% 
%     Part1: cost
%
% ******************
%
X = [ones(m,1) X];
h1=sigmoid(X*Theta1');

h1 = [ones(size(h1,1),1) h1];
h2 = sigmoid(h1*Theta2');

tmp=eye(num_labels);
Y=zeros(m, num_labels);

% y(i) is the label value 1~10 and also the index for 
% y= 1	 tmp= 1 0 0
%  	 1 		  0 1 0
% 	 3		  0 0 1
% so tmp(y(i),:) extract 100, 100, 001. so a vector transform to matrix

for i=1:m
	Y(i,:) = tmp(y(i),:);
end

J=sum(sum(-Y.*log(h2)-(1-Y).*log(1-h2)))/m;

t1=Theta1(:,2:size(Theta1)(2));
t2=Theta2(:,2:size(Theta2)(2));
P=(sum(sum(t1.*t1)) + sum(sum(t2.*t2))) * lambda ./ 2 ./ m;

J = J+P;

%
% ******************
% 
%     Part2: gradients
%
% ******************
%
delta_3 = h2 - Y;
delta_2 = delta_3*Theta2(:,2:end).*sigmoidGradient(X*Theta1');

delta_2_cap = delta_3'*h1;
delta_1_cap = delta_2'*X;

Theta1_grad = 1 / m * delta_1_cap + lambda/m * Theta1;
Theta2_grad = 1 / m * delta_2_cap + lambda/m * Theta2;

Theta1_grad(:,1) -= lambda/m * Theta1(:,1);
Theta2_grad(:,1) -= lambda/m * Theta2(:,1);

% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
